package com.ou.postservice.repository.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import com.ou.postservice.pojo.Post;


public interface PostRepository {
    Optional<List<Post>> loadPost(Long userId, @RequestParam Map<String, String> params, Long currentUserId);
    Optional<List<Post>> loadNewFeed(Long currentUserId, @RequestParam Map<String, String> params);
    boolean isResponse(Long postId, Long userId);
}
