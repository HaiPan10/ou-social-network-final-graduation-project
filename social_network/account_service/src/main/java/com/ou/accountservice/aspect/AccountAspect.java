package com.ou.accountservice.aspect;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ou.accountservice.event.AccountMailEvent;
import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.AuthResponse;
import com.ou.accountservice.pojo.Status;
import com.ou.accountservice.repository.repositoryJPA.AccountRepositoryJPA;

@Aspect
@Component
public class AccountAspect {
    @Autowired
    private AccountRepositoryJPA accountRepositoryJPA;

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @AfterReturning(pointcut = "execution(" +
            "public com.ou.accountservice.pojo.Account " +
            "com.ou.accountservice.service.interfaces.AccountService.changePassword(String, String))", returning = "account")
    public void changePassword(Account account) {
        if (account.getStatus().equals(Status.PASSWORD_CHANGE_REQUIRED.toString())) {
            account.setStatus(Status.ACTIVE.toString());
            account.setResetPasswordDate(
                    Date.from(LocalDateTime.now()
                            .plusHours(Long.valueOf(env.getProperty("HOURS_TIMEOUT")))
                            .atZone(ZoneId.systemDefault()).toInstant()));
            accountRepositoryJPA.save(account);
        }
    }

    // @AfterReturning(pointcut = "execution(" +
    //         "public com.ou.accountservice.pojo.AuthResponse " +
    //         "com.ou.accountservice.service.interfaces.AccountService.create(" +
    //         "com.ou.accountservice.pojo.Account, com.ou.accountservice.pojo.User, com.ou.accountservice.pojo.UserStudent))", 
    //         returning = "authResponse")
    // public void sendVerificationEmail(AuthResponse authResponse) {
    //     Optional<Account> accountOptional = accountRepositoryJPA.findById(authResponse.getUser().getId());
    //     if (accountOptional.isPresent()) {
    //         applicationEventPublisher.publishEvent(
    //             new AccountMailEvent(this, 
    //             "mailAccountTopic", 
    //             "sendVerificationEmail", 
    //             accountOptional.get().getId(), accountOptional.get().getEmail(),
    //             accountOptional.get().getVerificationCode(), accountOptional.get().getStatus(),
    //             accountOptional.get().getUser().getFirstName(), accountOptional.get().getUser().getLastName()));
    //     }
    // }
}
