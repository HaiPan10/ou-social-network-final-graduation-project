package com.ou.accountservice.aspect;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.Status;
import com.ou.accountservice.repository.repositoryJPA.AccountRepositoryJPA;

@Aspect
@Component
public class AccountAspect {
    @Autowired
    private AccountRepositoryJPA accountRepositoryJPA;

    @Autowired
    private Environment env;

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
}
