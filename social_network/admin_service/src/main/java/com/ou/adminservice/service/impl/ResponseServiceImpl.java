package com.ou.adminservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.Answer;
import com.ou.adminservice.pojo.Response;
import com.ou.adminservice.service.interfaces.ResponseService;

@Service
public class ResponseServiceImpl implements ResponseService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Response create(Response response) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public List<Answer> getTextAnswers(Long questionId) {
        return builder.build().get()
                .uri("http://post-service/api/responses",
                    uriBuilder -> uriBuilder.pathSegment("{id}").build(questionId))
                .retrieve()
                .bodyToFlux(Answer.class)
                .collectList()
                .block();
    }

}
