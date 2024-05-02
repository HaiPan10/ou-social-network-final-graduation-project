package com.ou.postservice.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.ou.postservice.event.PostEvent;
import com.ou.postservice.pojo.Post;

@Aspect
@Component
public class PostAspect {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @AfterReturning(pointcut = "execution(" +
            "public com.ou.postservice.pojo.Post " +
            "com.ou.postservice.service.interfaces.PostService.uploadPostSurvey(" +
            "com.ou.postservice.pojo.Post, Long))", 
            returning = "post")
    public void realtimePostSurvey(Post post) {
        applicationEventPublisher.publishEvent(
            new PostEvent(this, "postTopic", "realtimePost", post.getId(), "create"));
        applicationEventPublisher.publishEvent(
            new PostEvent(this, "postTopic", "realtimeProfile", post.getId(), "create"));
    }

    @AfterReturning(pointcut = "execution(" +
            "public com.ou.postservice.pojo.Post " +
            "com.ou.postservice.service.interfaces.PostService.uploadPost(" +
            "String, Long, java.util.List<org.springframework.web.multipart.MultipartFile>, boolean))", 
            returning = "newPost")
    public void realtimePost(Post newPost) {
        applicationEventPublisher.publishEvent(
            new PostEvent(this, "postTopic", "realtimePost", newPost.getId(), "create"));
        applicationEventPublisher.publishEvent(
            new PostEvent(this, "postTopic", "realtimeProfile", newPost.getId(), "create"));
    }
}
