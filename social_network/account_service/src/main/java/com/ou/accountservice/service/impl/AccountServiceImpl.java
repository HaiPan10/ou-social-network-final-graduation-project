package com.ou.accountservice.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ou.accountservice.configs.JwtService;
import com.ou.accountservice.pojo.UserDoc;
import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.AuthRequest;
import com.ou.accountservice.pojo.AuthResponse;
import com.ou.accountservice.pojo.Status;
import com.ou.accountservice.pojo.User;
import com.ou.accountservice.pojo.UserStudent;
import com.ou.accountservice.repository.repositoryJPA.AccountRepositoryJPA;
import com.ou.accountservice.service.interfaces.AccountService;
// import com.ou.accountservice.service.interfaces.FirebaseService;
// import com.ou.accountservice.service.interfaces.MailService;
import com.ou.accountservice.service.interfaces.RoleService;
import com.ou.accountservice.service.interfaces.ScheduleService;
import com.ou.accountservice.service.interfaces.UserService;
import com.ou.accountservice.service.interfaces.UserStudentService;

@Service("accountDetailService")
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepositoryJPA accountRepositoryJPA;

    // @Autowired
    // private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStudentService userStudentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ScheduleService scheduleService;
    // @Autowired
    // private FirebaseService firebaseService;
    // @Autowired
    // private MailService mailService;

    // @Autowired
    // private FirebaseAuth firebaseAuth;

    @Autowired
    private Environment env;

    @Override
    public Account retrieve(Long id) throws Exception {
        Optional<Account> accountOptional = accountRepositoryJPA.findById(id);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new Exception("Account not found");
        }
    }

    @Override
    public List<Account> search(Map<String, String> params) {
        int page;
        String kw;
        String status;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p) - 1;
            } else {
                page = 0;
            }

            kw = params.get("kw");
            if (kw == null || kw.isEmpty()) {
                kw = "_";
            } else {
                kw = kw.trim();
            }

            status = params.get("status");
            if (status == null) {
                status = "_";
            }

        } else {
            page = 0;
            kw = "_";
            status = "_";
        }
        int pageSize = Integer.parseInt(this.env.getProperty("PENDING_ACCOUNT_PAGE_SIZE"));

        Pageable pageable = PageRequest.of(page, pageSize);
        return accountRepositoryJPA.search(kw, status, pageable);
    }

    @Override
    public Account create(Account account) throws Exception {
        if (accountRepositoryJPA.findByEmail(account.getEmail()).isPresent()) {
            throw new Exception("Email này đã được sử dụng!");
        }
        account.setCreatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        String encoded = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encoded);
        account.setConfirmPassword(encoded);
        return accountRepositoryJPA.save(account);
    }

    // Hàm gọi khi sinh viên gởi yêu cầu tạo tài khoản
    @Override
    public AuthResponse create(Account account, User user, UserStudent userStudent) throws Exception {
        try {
            account.setRoleId(roleService.retrieve(Long.valueOf(1)));
            account.setStatus(Status.EMAIL_VERIFICATION_PENDING.toString());
            account.setVerificationCode(RandomStringUtils.randomAlphanumeric(64));
            create(account);
            String defaultAvatar = this.env.getProperty("DEFAULT_AVATAR").toString();
            String defaultCover = this.env.getProperty("DEFAULT_COVER").toString();
            user.setAvatar(defaultAvatar);
            user.setCoverAvatar(defaultCover);
            user.setAccount(account);
            userService.create(user);
            userStudent.setUser(user);
            userStudentService.create(userStudent);
            account.setUser(user);
            mailService.sendVerificationEmail(account.getId());
            String token = jwtService.generateAccessToken(account);
            return new AuthResponse(account.getRoleId(), user, token);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Account> getPendingAccounts(Map<String, String> params) {
        int page;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p) - 1;
            } else {
                page = 0;
            }
        } else {
            page = 0;
        }
        int pageSize = Integer.parseInt(this.env.getProperty("PENDING_ACCOUNT_PAGE_SIZE"));
        Pageable pageable = PageRequest.of(page, pageSize);
        return accountRepositoryJPA.getPendingAccounts(pageable);
    }

    @Override
    public Long countPendingAccounts() {
        return accountRepositoryJPA.countPendingAccounts();
    }

    @Override
    public boolean verifyAccount(Account account, String status) {
        try {
            // if(status.equals(Status.ACTIVE.toString())){
            // mailService.sendAcceptedMail(account);
            // } else if(status.equals(Status.REJECT.toString())){
            // mailService.sendRejectMail(account);
            // }
            switch (status) {
                case "ACTIVE":
                    if (account.getStatus().equals(Status.AUTHENTICATION_PENDING.toString())) {
                        UserDoc userDoc = new UserDoc();
                        userDoc.setDisplayName(String.format("%s %s", account.getUser().getLastName(),
                                account.getUser().getFirstName()));
                        userDoc.setPhotoUrl(account.getUser().getAvatar());
                        userDoc.setUserId(account.getUser().getId());
                        userDoc.setActiveStatus("offline");
                        firebaseService.saveOrUpdate(userDoc);
                        mailService.sendAcceptedMail(account);
                    } else if (account.getStatus().equals(Status.LOCKED.toString())) {
                        mailService.sendUnlockMail(account);
                    }
                    break;
                case "REJECT":
                    mailService.sendRejectMail(account);
                    break;
                case "LOCKED":
                    mailService.sendLockMail(account);
                    break;
                case "PASSWORD_CHANGE_REQUIRED":
                    mailService.sendResetPasswordRequire(account);
                    scheduleService.changePasswordRequiredSchedule(account.getEmail());
                    break;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] - System fail to send mail to client");
            System.out.println("[ERROR] - Message: " + e.getMessage());
        }

        try {
            accountRepositoryJPA.verifyAccount(account.getId(), status);
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean verifyEmail(Long accountId, String verificationCode) throws Exception {
        Account account = retrieve(accountId);
        if (!account.getStatus().equals("EMAIL_VERIFICATION_PENDING")) {
            throw new Exception("This account can't not be verified");
        }
        if (account.getVerificationCode().equals(verificationCode)) {
            return verifyAccount(account, "AUTHENTICATION_PENDING");
        } else {
            throw new Exception("Verification code doesn't match!");
        }
    }

    @Override
    public AuthResponse login(AuthRequest account) throws Exception {
        try {
            Optional<Account> accountOptional = accountRepositoryJPA.findByEmail(account.getEmail());
            if (!accountOptional.isPresent()) {
                throw new Exception("Email không tồn tại!");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            account.getEmail(), account.getPassword()));

            Account authenticationAccount = accountOptional.get();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateAccessToken(authenticationAccount);

            // if (!authenticationAccount.getStatus().equals("ACTIVE")) {
            // // EXCEPTION JSON FOR CLIENT
            // String jsonString = new JSONObject()
            // .put("id", authenticationAccount.getId())
            // .put("status", authenticationAccount.getStatus())
            // .put("accessToken", token)
            // .toString();
            // throw new Exception(jsonString);
            // }

            return new AuthResponse(authenticationAccount.getRoleId(), authenticationAccount.getUser(), token);
        } catch (AuthenticationException exception) {
            throw new Exception("Email hoặc mật khẩu không đúng.");
        }
    }

    @Override
    public String getStatus(Long accountId) throws InterruptedException, ExecutionException {
        String status = accountRepositoryJPA.getStatus(accountId);
        // if (status.equals("ACTIVE") || status.equals("PASSWORD_CHANGE_REQUIRED")) {
        //     UserDoc userDoc = firebaseService.retrieve(accountId.toString());
        //     userDoc.setActiveStatus("online");
        //     firebaseService.saveOrUpdate(userDoc);
        // }
        return status;
    }

    @Override
    public Account create(Account account, User user) throws Exception {
        try {
            account.setRoleId(roleService.retrieve(Long.valueOf(2)));
            account.setStatus(Status.PASSWORD_CHANGE_REQUIRED.toString());
            create(account);
            // System.out.println("[DEBUG] - Saved account id: " + account.getId());
            user.setAccount(account);
            userService.create(user);
            account.setUser(user);
            UserDoc userDoc = new UserDoc();
            userDoc.setDisplayName(String.format("%s %s", user.getLastName(), user.getFirstName()));
            userDoc.setPhotoUrl(user.getAvatar());
            userDoc.setUserId(user.getId());
            userDoc.setActiveStatus("offline");
            firebaseService.saveOrUpdate(userDoc);
            // UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            // .setEmail(account.getEmail())
            // .setPassword(env.getProperty("DEFAULT_PASSWORD").toString());
            // firebaseAuth.createUser(request);
            mailService.sendGrantedAccount(account);
            scheduleService.changePasswordRequiredSchedule(account.getEmail());
            return account;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Account changePassword(String changedPassword, String authPassword) throws Exception {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            // Optional<Account> optionalAccount =
            // accountRepository.findByEmail(account.getEmail());
            // if(!optionalAccount.isPresent()){
            // throw new Exception("Email không tồn tại");
            // };
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, authPassword));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account authAccount = accountRepositoryJPA.findByEmail(email).get();
            String encoded = bCryptPasswordEncoder.encode(changedPassword);
            authAccount.setPassword(encoded);
            authAccount.setConfirmPassword(encoded);
            // accountRepository.updateAccount(authAccount);
            return accountRepositoryJPA.save(authAccount);
        } catch (AuthenticationException exception) {
            throw new Exception("Mật khẩu không đúng.");
        }
    }

    @Override
    public Account loadAccountByEmail(String email) {
        return accountRepositoryJPA.findByEmail(email).get();
    }

    @Override
    public Long countAccounts(Map<String, String> params) {
        String kw;
        String status;
        if (params != null) {

            kw = params.get("kw");
            if (kw == null || kw.isEmpty()) {
                kw = "_";
            } else {
                kw = kw.trim();
            }

            status = params.get("status");
            if (status == null) {
                status = "_";
            }

        } else {
            kw = "_";
            status = "_";
        }

        return accountRepositoryJPA.countAccounts(kw, status);
    }

    @Override
    public List<Object[]> list() {
        return accountRepositoryJPA.list();
    }

    @Override
    public List<Object[]> stat(Map<String, String> params) throws Exception {
        try {
            boolean byMonth = params.get("byMonth") != null ? true : false;
            boolean byQuarter = params.get("byQuarter") != null ? true : false;
            Integer year = params.get("year") != null ? Integer.valueOf(params.get("year")) : null;
            return accountRepositoryJPA.stat(year, byMonth, byQuarter);
        } catch (Exception exception) {
            System.out.println("[DEBUG] - " + exception.getMessage());
            throw new Exception("Some things wrong");
        }
    }

}
