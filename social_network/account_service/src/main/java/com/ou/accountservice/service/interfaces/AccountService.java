package com.ou.accountservice.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.AccountNotFoundException;

import com.ou.accountservice.pojo.Account;
import com.ou.accountservice.pojo.AuthRequest;
import com.ou.accountservice.pojo.AuthResponse;
import com.ou.accountservice.pojo.User;
import com.ou.accountservice.pojo.UserStudent;


public interface AccountService{
    Account retrieve(Long id) throws Exception;
    Account retrieve(String email) throws Exception;
    List<Account> search(Map<String, String> params);
    Account create(Account account) throws Exception;
    AuthResponse create(Account account, User user, UserStudent userStudent) throws Exception;
    List<Account> getPendingAccounts(Map<String, String> params);
    Long countPendingAccounts();
    boolean verifyAccount(Account account, String status);
    boolean verifyEmail(Long accountId, String verificationCode) throws Exception;
    AuthResponse login(AuthRequest account) throws AccountNotFoundException, Exception;
    String getStatus(Long accountId) throws InterruptedException, ExecutionException;
    Account create(Account account, User user) throws Exception;
    Account changePassword(String changedPassword, String authPassword)throws Exception;
    Account loadAccountByEmail(String email);
    Long countAccounts(Map<String, String> params);
    List<Object[]> list();
    List<Object[]> stat(Map<String, String> params) throws Exception;
    Boolean validateToken(String token);
}
