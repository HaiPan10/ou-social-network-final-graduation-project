package com.ou.postservice.service.interfaces;

import java.util.List;

import com.ou.postservice.pojo.PostSurvey;
import com.ou.postservice.pojo.Question;

public interface QuestionService {
    List<Question> create(PostSurvey postSurvey, List<Question> question);
    List<Object[]> stat(Long questionId);
    Integer countUnchoiceOption(Long questionId);
    String getText(Long id);
}
