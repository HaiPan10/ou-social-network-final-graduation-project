package com.ou.adminservice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.PostInvitation;
import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.PostInvitationService;

@Service
public class PostInvitationServiceImpl implements PostInvitationService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public PostInvitation create(Long postId, PostInvitation postInvitation, List<User> listUsers) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Object[][] stat(Map<String, String> params) {
        return builder.build().get()
                .uri("http://post-service/api/posts/stat/post_invitation",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("year", Optional.ofNullable(params.get("year")))
                                .queryParamIfPresent("byMonth", Optional.ofNullable(params.get("byMonth")))
                                .queryParamIfPresent("byQuarter", Optional.ofNullable(params.get("byQuarter")))
                                .build())
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

}
