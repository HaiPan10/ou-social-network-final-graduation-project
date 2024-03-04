package com.ou.accountservice.service.impl;

import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.Status;
import com.ou.accountservice.repository.repositoryJPA.AccountRepositoryJPA;
import com.ou.accountservice.service.interfaces.AccountService;
import com.ou.accountservice.service.interfaces.ScheduleService;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    @Qualifier("scheduledExecutorService")
    private ScheduledExecutorService service;

    @Autowired
    private AccountRepositoryJPA accountRepositoryJPA;
    
    @Autowired
    private Environment env;
    @Autowired
    private AccountService accountService;

    @Override
    public void changePasswordRequiredSchedule(String email) {
        Runnable runnable = () -> {
            Optional<Account> account = accountRepositoryJPA.findByEmail(email);
            if(account.isPresent()){
                if(account.get().getStatus().equals(Status.PASSWORD_CHANGE_REQUIRED.toString())){
                    accountService.verifyAccount(account.get(), Status.LOCKED.toString());
                    System.out.printf("[DEBUG] - Email %s has been locked by system\n", account.get().getEmail());
                }
            }
            System.out.println("[DEBUG] - Task is done");
        };

        System.out.println("[DEBUG] - Email is wait for changing password");
        Long hoursTimeout = Long.parseLong(env.getProperty("HOURS_TIMEOUT"));
        service.schedule(runnable, hoursTimeout, TimeUnit.HOURS);
    }
    
}
