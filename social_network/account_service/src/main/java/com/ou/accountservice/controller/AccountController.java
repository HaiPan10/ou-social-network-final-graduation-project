package com.ou.accountservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.AuthRequest;
import com.ou.accountservice.pojo.AuthResponse;
import com.ou.accountservice.pojo.User;
import com.ou.accountservice.pojo.UserStudent;
import com.ou.accountservice.service.interfaces.AccountService;
import com.ou.accountservice.utils.ValidationUtils;
import com.ou.accountservice.validator.MapValidator;
import com.ou.accountservice.validator.WebAppValidator;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/accounts")
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MapValidator mapValidator;

    @Autowired
    private WebAppValidator webAppValidator;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Environment env;

    @InitBinder("params")
    public void initBinderMap(WebDataBinder binder) {
        binder.setValidator(mapValidator);
    }

    @InitBinder("requestBody")
    public void initBinderWeb(WebDataBinder binder) {
        binder.setValidator(webAppValidator);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> createPendingAccount(@Valid @RequestBody Account requestBody,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
        } else {
            try {
                Account createAccount = new Account(requestBody.getEmail(), requestBody.getPassword(),
                        requestBody.getConfirmPassword());
                User createUser = new User(requestBody.getUser().getFirstName(), requestBody.getUser().getLastName());
                UserStudent createStudent = new UserStudent(
                        requestBody.getUser().getUserStudent().getStudentIdentical());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(accountService.create(createAccount, createUser, createStudent));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @GetMapping(path = "/verify/{accountId}/{verificationCode}")
    public ResponseEntity<Object> verifyAccount(@PathVariable Long accountId,
            @PathVariable String verificationCode, ServerHttpResponse response) throws Exception {
        try {
            if (accountService.verifyEmail(accountId, verificationCode)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(String.format("%s/", env.getProperty("CLIENT_HOSTNAME"))));
                return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
            } else {
                return null;
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest requestBody,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
            }
            AuthResponse response = accountService.login(requestBody);
            if (response != null) {
                return ResponseEntity.ok().body(response);
            } else {
                throw new Exception("Get Null Pointer");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/status")
    public ResponseEntity<Object> getStatus(@RequestHeader HttpHeaders headers) {
        try {
            Long accountId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(accountService.getStatus(accountId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, Object> params,
            BindingResult bindingResult, @RequestHeader HttpHeaders headers) {
        try {
            Long id = Long.parseLong(headers.getFirst("AccountID"));
            String password = mapper.convertValue(params.get("password"), String.class);
            String authPassword = mapper.convertValue(params.get("authPassword"), String.class);
            String confirmPassword = mapper.convertValue(params.get("confirmPassword"), String.class);
            // mapValidator.validate(params, bindingResult);
            if (bindingResult.hasErrors() || !password.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("Mật khẩu không khớp");
            }
            accountService.changePassword(id, password, authPassword);

            return ResponseEntity.ok().body("Change Password Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> search(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.ok().body(accountService.search(params).stream()
                    .filter(acc -> (acc.getStatus().equals("ACTIVE")
                            || acc.getStatus().equals("PASSWORD_CHANGE_REQUIRED")))
                    .map(account -> account.getUser()).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/email")
    public ResponseEntity<?> getAccount(@RequestParam String email) {
        try {
            return ResponseEntity.ok().body(accountService.retrieve(email));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // This API will be called by the api gateway for access permit
    @GetMapping("/validate")
    public Mono<Boolean> validateToken(@RequestParam String token) {
        return Mono.just(accountService.validateToken(token));
    }

    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveAccount(@RequestParam Long accountId) {
        try {
            return ResponseEntity.ok().body(accountService.retrieve(accountId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending/accounts")
    public List<Account> pendingAccounts(@RequestParam Map<String, String> params) {
        return accountService.getPendingAccounts(params);
    }

    @GetMapping("/pending/count")
    public Long countPendingAccounts() {
        return accountService.countPendingAccounts();
    }

    @GetMapping("/verify/{accountId}")
    public ResponseEntity<?> verifyAccount(@PathVariable Long accountId, @RequestParam String status) {
        // Verify pending account
        try {
            Account account = accountService.retrieve(accountId);
            return ResponseEntity.ok().body(accountService.verifyAccount(account, status));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Account account) {
        try {
            User user = account.getUser();
            account.setUser(null);
            Account createdAccount = accountService.create(account, user);
            return ResponseEntity.ok().body(createdAccount);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public Long countAccounts(@RequestParam Map<String, String> params) {
        return accountService.countAccounts(params);
    }

    @GetMapping(path = "/search-admin")
    public ResponseEntity<Object> searchAdmin(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.ok().body(accountService.search(params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
