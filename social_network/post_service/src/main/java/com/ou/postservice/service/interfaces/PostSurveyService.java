package com.ou.postservice.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ou.postservice.pojo.PostSurvey;


public interface PostSurveyService {
    PostSurvey uploadPostSurvey(Long postId, PostSurvey postSurvey) throws Exception;
    List<Object[]> stat(Map<String, String> params);
}
