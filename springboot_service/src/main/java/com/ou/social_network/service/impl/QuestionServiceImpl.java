package com.ou.social_network.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.PostSurvey;
import com.ou.social_network.pojo.Question;
import com.ou.social_network.pojo.QuestionOption;
import com.ou.social_network.repository.repositoryJPA.QuestionRepositoryJPA;
import com.ou.social_network.service.interfaces.QuestionService;


@Service
public class QuestionServiceImpl implements QuestionService{
    // @Autowired
    // private QuestionRepository questionRepository;

    @Autowired
    private QuestionRepositoryJPA questionRepositoryJPA;

    @Override
    public List<Question> create(PostSurvey postSurvey, List<Question> question) {
        question.forEach(q -> {
            q.setSurveyId(postSurvey);
            List<QuestionOption> questionOptions = q.getQuestionOptions();
            q.setQuestionOptions(null);
            if (questionOptions != null) {
                questionOptions.forEach(qo -> {
                    qo.setQuestionId(q);
                });
                q.setQuestionOptions(questionOptions);
            }
        });
        return questionRepositoryJPA.saveAll(question);
    }

    @Override
    public List<Object[]> stat(Long questionId) {
        return questionRepositoryJPA.stat(questionId);
    }

    @Override
    public Integer countUnchoiceOption(Long questionId) {
        return questionRepositoryJPA.countUnchoiceOption(questionId);
    }

    @Override
    public String getText(Long id) {
        return questionRepositoryJPA.getText(id);
    }
    
}
