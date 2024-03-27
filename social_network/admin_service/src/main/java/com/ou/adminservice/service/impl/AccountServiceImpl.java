package com.ou.adminservice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ou.adminservice.pojo.Account;
import com.ou.adminservice.service.interfaces.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

    @Override
    public List<Object[]> list() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public List<Object[]> stat(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stat'");
    }

    @Override
    public Long countAccounts(Map<String, String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countAccounts'");
    }

    @Override
    public Long countPendingAccounts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countPendingAccounts'");
    }

    @Override
    public List<Account> getPendingAccounts(Map<String, String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPendingAccounts'");
    }

    @Override
    public List<Account> search(Map<String, String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public Account retrieve(Long id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }

    @Override
    public Account retrieve(String email) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }
    
}
