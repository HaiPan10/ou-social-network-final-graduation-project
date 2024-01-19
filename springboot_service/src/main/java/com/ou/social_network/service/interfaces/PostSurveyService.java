package com.ou.social_network.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.social_network.pojo.PostSurvey;


public interface PostSurveyService {
    PostSurvey uploadPostSurvey(Long postId, PostSurvey postSurvey) throws Exception;
    List<Object[]> stat(Map<String, String> params);
}
