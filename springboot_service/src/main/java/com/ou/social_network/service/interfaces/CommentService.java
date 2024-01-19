package com.ou.social_network.service.interfaces;

import java.util.List;

import com.ou.social_network.pojo.Comment;


public interface CommentService {
    Integer countComment(Long postId);
    Comment create(Comment comment, Long postId, Long userId) throws Exception;
    Comment create(Comment comment, Long postId, Long userId, Long commentId) throws Exception;
    List<Comment> loadComment(Long postId);
    List<Comment> loadComment(Long postId, Long commentId);
    Comment editComment(Comment comment) throws Exception;
    Comment retrieve(Long commentId) throws Exception;
    boolean delete(Long commentId, Long userId) throws Exception;
    List<Comment> loadTwoComments(Long postId);
    Comment getReplyInfo(Comment comment);
}
