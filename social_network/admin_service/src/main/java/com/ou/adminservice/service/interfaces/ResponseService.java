package com.ou.adminservice.service.interfaces;

import java.util.List;

import com.ou.adminservice.pojo.Answer;


public interface ResponseService {
    List<Answer> getTextAnswers(Long questionId);
}
