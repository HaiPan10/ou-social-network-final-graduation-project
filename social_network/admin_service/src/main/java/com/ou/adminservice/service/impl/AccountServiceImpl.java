package com.ou.adminservice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.Account;
import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.AccountService;

import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private WebClient.Builder builder;

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
        return builder.build().get()
                .uri("http://account-service/api/accounts/pending/count")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    @Override
    public List<Account> getPendingAccounts(Map<String, String> params) {
        int page;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p);
            } else {
                page = 1;
            }
        } else {
            page = 1;
        }
        return builder.build().get()
                .uri("http://account-service/api/accounts/pending/accounts",
                        uriBuilder -> uriBuilder
                            .queryParam("page", page)
                            .build())
                .retrieve()
                .bodyToFlux(Account.class)
                .collect(Collectors.toList())
                .block();
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

    @Override
    public boolean verifyAccount(Long accountId, String status) {
        return builder.build().get()
                .uri("http://account-service/api/accounts/pending/verification",
                        uriBuilder -> uriBuilder
                                .pathSegment("{accountId}")
                                .queryParam("status", status)
                                .build(accountId))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> Mono.just(false))
                .block();
    }

    @Override
    public Account create(Account account, User user) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

}
