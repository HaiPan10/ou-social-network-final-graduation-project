package com.ou.social_network.service.interfaces;

import java.util.List;

import com.ou.social_network.pojo.PostSurvey;
import com.ou.social_network.pojo.Question;

public interface QuestionService {
    List<Question> create(PostSurvey postSurvey, List<Question> question);
    List<Object[]> stat(Long questionId);
    Integer countUnchoiceOption(Long questionId);
    String getText(Long id);
}
