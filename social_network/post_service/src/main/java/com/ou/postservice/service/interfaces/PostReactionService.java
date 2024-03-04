package com.ou.postservice.service.interfaces;

import java.util.List;

import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.PostReaction;
import com.ou.postservice.pojo.Reaction;
import com.ou.postservice.pojo.User;


public interface PostReactionService {
    void countReaction(Post returnPost, Long currentUser);
    PostReaction reaction(Long postId, Long userId, Reaction reaction) throws Exception;
    boolean delete(Long postId, Long userId) throws Exception;
    List<User> getReactionUsers(Long postId, Long reactionId);
}
