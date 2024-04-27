package com.ou.adminservice.service.interfaces;

import java.util.List;

import com.ou.adminservice.pojo.Answer;
import com.ou.adminservice.pojo.Response;


public interface ResponseService {
    Response create(Response response) throws Exception;
    List<Answer> getTextAnswers(Long questionId);
}
