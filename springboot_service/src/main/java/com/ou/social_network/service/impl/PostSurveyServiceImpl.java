package com.ou.social_network.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.PostSurvey;
import com.ou.social_network.repository.repositoryJPA.PostSurveyRepositoryJPA;
import com.ou.social_network.service.interfaces.PostSurveyService;

@Service
public class PostSurveyServiceImpl implements PostSurveyService {
    // @Autowired
    // private PostSurveyRepository postSurveyRepository;
    @Autowired
    private PostSurveyRepositoryJPA postSurveyRepositoryJPA;

    @Override
    public PostSurvey uploadPostSurvey(Long postId, PostSurvey postSurvey) throws Exception {
        postSurvey.setStartAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        postSurvey.setId(postId);
        return postSurveyRepositoryJPA.save(postSurvey);
    }

    @Override
    public List<Object[]> stat(Map<String, String> params) {
        boolean byMonth = params.get("byMonth") != null ? true : false;
        boolean byQuarter = params.get("byQuarter") != null ? true : false;
        Integer year = params.get("year") != null ? Integer.valueOf(params.get("year")) : null;
        return postSurveyRepositoryJPA.stat(year, byMonth, byQuarter);
    }

}
