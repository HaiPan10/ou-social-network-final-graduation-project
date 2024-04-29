package com.ou.adminservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.service.interfaces.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Object[][] stat(Long questionId) {
        return builder.build().get()
                .uri("http://post-service/api/quests/stat",
                    uriBuilder -> uriBuilder.pathSegment("{questionId}").build(questionId))
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

    @Override
    public Integer countUnchoiceOption(Long questionId) {
        return builder.build().get()
                .uri("http://post-service/api/quests/stat/count",
                    uriBuilder -> uriBuilder.pathSegment("{questionId}").build(questionId))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    @Override
    public String getText(Long id) {
        return builder.build().get()
                .uri("http://post-service/api/quests/text",
                    uriBuilder -> uriBuilder.pathSegment("{questionId}").build(id))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}