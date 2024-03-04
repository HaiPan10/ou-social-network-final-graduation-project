package com.ou.postservice.service.interfaces;

import java.util.List;

import com.ou.postservice.pojo.Answer;
import com.ou.postservice.pojo.Response;


public interface ResponseService {
    Response create(Response response) throws Exception;
    List<Answer> getTextAnswers(Long questionId);
}
