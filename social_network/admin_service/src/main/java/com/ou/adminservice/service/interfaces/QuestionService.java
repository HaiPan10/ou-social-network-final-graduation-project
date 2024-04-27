package com.ou.adminservice.service.interfaces;

import java.util.List;

import com.ou.adminservice.pojo.PostSurvey;
import com.ou.adminservice.pojo.Question;

public interface QuestionService {
    List<Question> create(PostSurvey postSurvey, List<Question> question);
    Object[][] stat(Long questionId);
    Integer countUnchoiceOption(Long questionId);
    String getText(Long id);
}
