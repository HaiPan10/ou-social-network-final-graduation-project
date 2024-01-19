package com.ou.social_network.service.interfaces;

import java.util.List;

import com.ou.social_network.pojo.Answer;
import com.ou.social_network.pojo.Response;


public interface ResponseService {
    Response create(Response response) throws Exception;
    List<Answer> getTextAnswers(Long questionId);
}
