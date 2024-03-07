// package com.ou.mailservice.service.impl;

// import java.util.Calendar;
// import java.util.List;
// import java.util.concurrent.ExecutorService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.core.env.Environment;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.JavaMailSenderImpl;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// import com.ou.mailservice.pojo.Account;
// import com.ou.mailservice.pojo.PostInvitation;
// import com.ou.mailservice.pojo.Status;
// import com.ou.mailservice.pojo.User;
// // import com.ou.mailservice.service.interfaces.AccountService;
// import com.ou.mailservice.service.interfaces.MailService;

// import jakarta.mail.internet.MimeMessage;


// @Service
// public class MailServiceImpl implements MailService {
//     @Autowired
//     private JavaMailSender mailSender;

//     @Autowired
//     private Environment env;
    
//     @Autowired
//     private AccountService accountService;

//     // @Autowired
//     // private SimpleDateFormat simpleDateFormat;

//     @Autowired
//     @Qualifier("executorService")
//     private ExecutorService executorService;

//     @Override
//     public void sendEmail(String userEmail, String subject, String content) {
//         try {
//             MimeMessage mimeMessage = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//             helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
//             helper.setTo(userEmail);
//             helper.setSubject(subject);
//             helper.setText(content, true);
//             mailSender.send(mimeMessage);
//         } catch (Exception mex) {
//             mex.printStackTrace();
//         }
//     }

//     @Override
//     public void sendVerificationEmail(Long accountId) throws Exception {
//         Account account = accountService.retrieve(accountId);
//         if (account == null) {
//             throw new Exception("Không tìm thấy tài khoản!");
//         } else if (!account.getStatus().equals(Status.EMAIL_VERIFICATION_PENDING.toString())) {
//             throw new Exception("Trạng thái không hợp lệ!");
//         } else {
//             String mailBody = String.format("Xin chào %s,<br>"
//                     + "Cảm ơn bạn đã đăng kí tài khoản mạng xã hội cựu sinh viên trường đại học Mở TP.HCM<br>"
//                     + "Vui lòng nhấn vào đường link bên dưới để xác thực<br>"
//                     + "<h3><a href=\"%s\">XÁC THỰC NGAY</a></h3>"
//                     + "Chúng tôi xin cảm ơn,<br>"
//                     + "OU Social Network", account.getUser().getFirstName(),
//                     String.format("%s/api/accounts/verify/%d/%s",
//                             env.getProperty("SERVER_HOSTNAME"),
//                             account.getId(),
//                             account.getVerificationCode()));

//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Xác thực email", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendGrantedAccount(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Tài khoản không tồn tại!");
//         } else {
//             String mailBody = String.format("Kính gửi thầy/cô %s,<br>"
//                     + "<p>Tài khoản mạng xã hội cựu sinh viên trường đại học Mở TP.HCM của thầy/cô đã được kích hoạt</p>"
//                     + "Thông tin tài khoản bao gồm:<br>"
//                     + "Email: %s<br>"
//                     + "Password: ou@123<br>"
//                     + "Xin quý thầy cô hãy đổi password trong vòng 24h hoặc tài khoản sẽ bị khóa.<br>"
//                     + "Chúng tôi xin cảm ơn sự quan tâm của quý thầy cô,<br>"
//                     + "OU Social Network",
//                     String.format("%s %s", account.getUser().getLastName(), account.getUser().getFirstName()),
//                     account.getEmail());
//             // sendEmail(account.getEmail(), "Thông tin cấp tài khoản", mailBody);
//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Thông tin cấp tài khoản", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendAcceptedMail(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Không tìm thấy tài khoản!");
//         } else if (!account.getStatus().equals(Status.AUTHENTICATION_PENDING.toString())) {
//             throw new Exception("Trạng thái không hợp lệ!");
//         } else {
//             String mailBody = String.format("Xin chào %s,<br>"
//                     + "Cảm ơn bạn đã đăng kí tài khoản mạng xã hội cựu sinh viên trường đại học Mở TP.HCM.<br>"
//                     + "Tài khoản của bạn đã được kích hoạt thành công.<br>"
//                     + "Hãy nhấn vào đường link bên dưới để truy cập mạng xã hội.<br>"
//                     + "<h3><a href=\"%s\">OU SOCIAL NETWORK</a></h3>"
//                     + "Chúng tôi xin cảm ơn,<br>"
//                     + "OU Social Network",
//                     account.getUser().getFirstName(),
//                     String.format("%s/",
//                             env.getProperty("CLIENT_HOSTNAME")));
//             // sendEmail(account.getEmail(), "Trạng thái kích hoạt tài khoản", mailBody);
//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Trạng thái kích hoạt tài khoản", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendRejectMail(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Không tìm thấy tài khoản!");
//         } else if (!account.getStatus().equals(Status.AUTHENTICATION_PENDING.toString())) {
//             throw new Exception("Trạng thái không hợp lệ!");
//         } else {
//             String mailBody = String.format("Xin chào %s,<br>"
//                     + "Cảm ơn bạn đã đăng kí tài khoản mạng xã hội cựu sinh viên trường đại học Mở TP.HCM.<br>"
//                     + "Tài khoản của bạn không hợp lệ vui lòng liên hệ quản trị viên để cung cấp thêm thông tin.<br>"
//                     + "Chúng tôi xin cảm ơn,<br>"
//                     + "OU Social Network",
//                     account.getUser().getFirstName());
//             // sendEmail(account.getEmail(), "Trạng thái kích hoạt tài khoản", mailBody);

//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Trạng thái kích hoạt tài khoản", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendLockMail(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Tài khoản không tồn tại!");
//         } else {
//             String mailBody = String.format("Kính chào %s,<br>"
//                     + "<p>Tài khoản mạng mạng xã hội cựu sinh viên trường đại học Mở TP.HCM của bạn đã bị khóa bởi quản trị viên.</p>"
//                     + "Xin vui lòng liên hệ quản trị viên để thêm thông tin chi tiết.<br>"
//                     + "Chúng tôi xin cảm ơn sự đóng góp của bạn cho mạng xã hội cựu sinh viên,<br>"
//                     + "OU Social Network",
//                     String.format("%s %s", account.getUser().getLastName(), account.getUser().getFirstName()));
//             // sendEmail(account.getEmail(), "Tài khoản bị khóa", mailBody);

//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Tài khoản bị khóa", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendUnlockMail(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Tài khoản không tồn tại!");
//         } else {
//             String mailBody = String.format("Kính chào %s,<br>"
//                     + "<p>Tài khoản mạng mạng xã hội cựu sinh viên trường đại học Mở TP.HCM của bạn đã được gỡ khóa.</p>"
//                     + "Hãy nhấn vào đường link để truy cập trang web: %s.<br>"
//                     + "Chúng tôi xin cảm ơn sự đóng góp của bạn cho mạng xã hội cựu sinh viên,<br>"
//                     + "OU Social Network",
//                     String.format("%s %s", account.getUser().getLastName(), account.getUser().getFirstName()),
//                     env.getProperty("CLIENT_HOSTNAME"));
//             // sendEmail(account.getEmail(), "Tài khoản được gỡ khóa", mailBody);

//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Tài khoản được gỡ khóa", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendResetPasswordRequire(Account account) throws Exception {
//         if (account == null) {
//             throw new Exception("Tài khoản không tồn tại!");
//         } else {
//             String mailBody = String.format("Kính Chào %s,<br>"
//                     + "<p>Tài khoản mạng mạng xã hội cựu sinh viên trường đại học Mở TP.HCM của bạn đã được đặt lại mật khẩu mặc định.</p>"
//                     + "Xin vui lòng đổi mật khẩu của bạn trong vòng 24 tiếng nếu không sẽ tiếp tục bị khóa.<br>"
//                     + "Thông tin chi tiết tài khoản: <br>"
//                     + "Email: %s<br>"
//                     + "Password: ou@123<br>"
//                     + "Hãy nhấn vào đường link để truy cập trang web: %s.<br>"
//                     + "Chúng tôi xin cảm ơn sự quan tâm của bạn đối với mạng xã hội cho cựu sinh viên,<br>"
//                     + "OU Social Network",
//                     String.format("%s %s", account.getUser().getLastName(), account.getUser().getFirstName()),
//                     account.getEmail(),
//                     env.getProperty("CLIENT_HOSTNAME"));
//             // sendEmail(account.getEmail(), "Đặt lại mật khẩu mặc định", mailBody);

//             Runnable runnable = () -> {
//                 sendEmail(account.getEmail(), "Đặt lại mật khẩu mặc định", mailBody);
//             };

//             executorService.execute(runnable);
//         }
//     }

//     @Override
//     public void sendMultipleEmail(List<User> listUser, PostInvitation postInvitation) {
//         listUser.parallelStream().forEach(u -> {
//             sendEmail(u.getAccount().getEmail(), postInvitation.getEventName(), postInvitation.getPost().getContent());
//         });
//         System.out.println("[DEBUG] - FINISH SENDING MAIL TO ALL TARGET " + Calendar.getInstance().getTimeInMillis());
//     }
// }