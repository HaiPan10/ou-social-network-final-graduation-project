package com.ou.adminservice.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.adminservice.pojo.Account;

public interface AccountService {
    Object[][] list();
    Object[][] stat(Map<String, String> params) throws Exception;
    Long countAccounts(Map<String, String> params);
    Long countPendingAccounts();
    List<Account> getPendingAccounts(Map<String, String> params);
    List<Account> search(Map<String, String> params);
    Account retrieve(Long id) throws Exception;
    boolean verifyAccount(Long accountId, String status);
    Account create(Account account) throws Exception;
}
