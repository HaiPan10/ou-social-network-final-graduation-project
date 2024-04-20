package com.ou.adminservice.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.Account;
import com.ou.adminservice.service.interfaces.AccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private Environment env;

    @Override
    public Object[][] list() {
        return builder.build().get()
                .uri("http://account-service/api/accounts")
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

    @Override
    public Object[][] stat(Map<String, String> params) throws Exception {
        return builder.build().get()
                .uri("http://account-service/api/accounts/stat/users",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("year", Optional.ofNullable(params.get("year")))
                                .queryParamIfPresent("byMonth", Optional.ofNullable(params.get("byMonth")))
                                .queryParamIfPresent("byQuarter", Optional.ofNullable(params.get("byQuarter")))
                                .build())
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

    @Override
    public Long countAccounts(Map<String, String> params) {
        return builder.build().get()
                .uri("http://account-service/api/accounts/count",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("page", Optional.ofNullable(params.get("page")))
                                .queryParamIfPresent("kw", Optional.ofNullable(params.get("kw")))
                                .queryParamIfPresent("status", Optional.ofNullable(params.get("status")))
                                .build())
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(err -> Mono.empty())
                .block();
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
        return builder.build().get()
                .uri("http://account-service/api/accounts/search-admin",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("page", Optional.ofNullable(params.get("page")))
                                .queryParamIfPresent("kw", Optional.ofNullable(params.get("kw")))
                                .queryParamIfPresent("status", Optional.ofNullable(params.get("status")))
                                .build())
                .retrieve()
                .bodyToFlux(Account.class)
                .collect(Collectors.toList())
                .onErrorResume(err -> Mono.empty())
                .block();
    }

    @Override
    public Account retrieve(Long id) throws Exception {
        return builder.build().get()
                .uri("http://account-service/api/accounts/retrieve",
                        uriBuilder -> uriBuilder.queryParam("accountId", id).build())
                .retrieve()
                .bodyToMono(Account.class)
                .onErrorMap(ex -> new Exception("Error retrieving account: " + ex.getMessage()))
                .block();
    }

    @Override
    public Account retrieve(String email) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }

    @Override
    public boolean verifyAccount(Long accountId, String status) {
        return builder.build().get()
                .uri("http://account-service/api/accounts/verify",
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
    public Account create(Account account) throws Exception {
        return builder.build().post()
                .uri("http://account-service/api/accounts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchangeToMono(res -> {
                    {
                        if (res.statusCode().is2xxSuccessful()) {
                            return res.bodyToMono(Account.class);
                        }

                        return res.bodyToMono(String.class)
                                .flatMap(message -> Mono.error(new Exception(message)));
                    }
                })
                .onErrorMap(ex -> new Exception(ex.getMessage()))
                .block();
    }

}
