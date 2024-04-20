package com.ou.adminservice.service.interfaces;

import java.util.Map;

import com.ou.adminservice.pojo.PostSurvey;


public interface PostSurveyService {
    PostSurvey uploadPostSurvey(Long postId, PostSurvey postSurvey) throws Exception;
    Object[][] stat(Map<String, String> params);
}
