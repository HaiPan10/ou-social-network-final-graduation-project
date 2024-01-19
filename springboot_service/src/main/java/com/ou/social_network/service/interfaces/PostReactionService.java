package com.ou.social_network.service.interfaces;

import java.util.List;

import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.PostReaction;
import com.ou.social_network.pojo.Reaction;
import com.ou.social_network.pojo.User;


public interface PostReactionService {
    void countReaction(Post returnPost, Long currentUser);
    PostReaction reaction(Long postId, Long userId, Reaction reaction) throws Exception;
    boolean delete(Long postId, Long userId) throws Exception;
    List<User> getReactionUsers(Long postId, Long reactionId);
}
