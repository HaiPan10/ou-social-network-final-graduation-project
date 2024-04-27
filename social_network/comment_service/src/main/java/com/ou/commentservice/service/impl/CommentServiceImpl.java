package com.ou.commentservice.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.commentservice.event.CommentEvent;
import com.ou.commentservice.event.CommentTotalEvent;
import com.ou.commentservice.event.NotificationEvent;
import com.ou.commentservice.event.ReplyEvent;
import com.ou.commentservice.pojo.Comment;
import com.ou.commentservice.pojo.User;
import com.ou.commentservice.repository.repositoryJPA.CommentRepositoryJPA;
import com.ou.commentservice.service.interfaces.CommentService;
import com.ou.commentservice.pojo.Post;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepositoryJPA commentRepositoryJPA;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Integer countComment(Long postId) {
        return commentRepositoryJPA.countComment(postId);
    }

    @Override
    public Comment create(Comment comment, Long postId, Long userId) throws Exception {
        Post persistPost = webClientBuilder.build().get()
            .uri("http://post-service/api/posts/fetch-post",
            uriBuilder -> uriBuilder.queryParam("postId", postId).build())
            .retrieve()
            .bodyToMono(Post.class)
            .block();
        if (!persistPost.getIsActiveComment()) {
            throw new Exception("Bài đăng này bị khóa bình luận");
        } else {
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setCreatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            comment.setUpdatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            commentRepositoryJPA.save(comment);

            // List<SocketClient> socketClients = socketService
            //         .getSocketClientsForPath(String.format("/comment/%s", postId));
            // boolean userIdExists = socketClients.stream()
            //         .anyMatch(client -> client.getId().equals(persistPost.getUserId().getId()));
            if (!persistPost.getUserId().equals(userId)) {
                applicationEventPublisher.publishEvent(
                    new NotificationEvent(this, "notificationTopic", "comment",
                    postId, comment.getId(), comment.getContent(), false, userId, persistPost.getUserId()));
            }

            if (comment.getParentComment() == null) {
                applicationEventPublisher.publishEvent(
                    new CommentEvent(this, "commentTopic", comment.getId(), postId, "CREATE"));
            } else {
                Comment parentComment = comment.getParentComment();
                parentComment = getReplyInfo(parentComment);
                applicationEventPublisher.publishEvent(
                    new CommentEvent(this, "commentTopic", parentComment.getId(), postId, "UPDATE"));
                applicationEventPublisher.publishEvent(
                    new ReplyEvent(this, "replyTopic", comment.getId(), parentComment.getId(), "CREATE"));

                if (!comment.getUserId().equals(comment.getRepliedUserId())) {
                    applicationEventPublisher.publishEvent(
                        new NotificationEvent(this, "notificationTopic", "reply",
                        postId, comment.getId(), comment.getParentComment().getId(),
                        comment.getContent(), false, userId, comment.getRepliedUserId()));
                }

            }
            applicationEventPublisher.publishEvent(
                new CommentTotalEvent(this, "commentTotalTopic", postId));
            return comment;
        }
    }

    @Override
    public List<Comment> loadComment(Long postId) {
        List<Comment> comments = commentRepositoryJPA.loadComment(postId);
        comments.forEach(comment -> {
            comment.setRepliesTotal(commentRepositoryJPA.countReply(postId, comment.getId()));
            Comment firstReply = commentRepositoryJPA.getFirstReply(postId, comment.getId());
            if (firstReply != null) {
                firstReply.setUser(
                    webClientBuilder.build().get()
                        .uri("http://account-service/api/users",
                        uriBuilder -> uriBuilder.queryParam("userId", firstReply.getUserId()).build())
                        .retrieve()
                        .bodyToMono(User.class)
                        .block());
            }
            comment.setFirstReply(firstReply);
            if (comment.getParentComment() != null) {
                comment.setRepliedUser(
                    webClientBuilder.build().get()
                        .uri("http://account-service/api/users",
                        uriBuilder -> uriBuilder.queryParam("userId", comment.getRepliedUserId()).build())
                        .retrieve()
                        .bodyToMono(User.class)
                        .block()
                );
            }
            comment.setUser(
                webClientBuilder.build().get()
                    .uri("http://account-service/api/users",
                    uriBuilder -> uriBuilder.queryParam("userId", comment.getUserId()).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block()
            );
        });
        return comments;
    }

    @Override
    public Comment editComment(Comment comment) throws Exception {
        Comment persistComment = retrieve(comment.getId());
        if (!persistComment.getUserId().equals(comment.getUserId())) {
            throw new Exception("Not owner");
        }
        persistComment.setContent(comment.getContent());
        persistComment.setUpdatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        commentRepositoryJPA.save(persistComment);
        if (persistComment.getParentComment() == null) {
            applicationEventPublisher.publishEvent(
                    new CommentEvent(this, "commentTopic", persistComment.getId(), persistComment.getPostId(), "UPDATE"));
        } else {
            Comment parentComment = persistComment.getParentComment();
            applicationEventPublisher.publishEvent(
                    new ReplyEvent(this, "replyTopic", persistComment.getId(), parentComment.getId(), "UPDATE"));
        }
        applicationEventPublisher.publishEvent(
                new CommentTotalEvent(this, "commentTotalTopic", persistComment.getPostId()));
        return persistComment;
    }

    @Override
    public Comment retrieve(Long commentId) throws Exception {
        Optional<Comment> commentOptional = commentRepositoryJPA.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            User user = webClientBuilder.build().get()
                .uri("http://account-service/api/users",
                uriBuilder -> uriBuilder.queryParam("userId", comment.getUserId()).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
            comment.setUser(user);
            if (comment.getRepliedUserId() != null) {
                User repliedUser = webClientBuilder.build().get()
                    .uri("http://account-service/api/users",
                    uriBuilder -> uriBuilder.queryParam("userId", comment.getRepliedUserId()).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
                comment.setRepliedUser(repliedUser);
            } else {
                comment.setRepliedUser(null);
            }
            return comment;
        } else {
            throw new Exception("Không tìm thấy comment!");
        }
    }

    @Override
    public boolean delete(Long commentId, Long userId) throws Exception {
        Comment persistComment = retrieve(commentId);
        Long postId = persistComment.getPostId();
        Post persistPost = webClientBuilder.build().get()
            .uri("http://post-service/api/posts/fetch-post",
            uriBuilder -> uriBuilder.queryParam("postId", postId).build())
            .retrieve()
            .bodyToMono(Post.class)
            .block();
        persistComment.setPost(persistPost);
        if (!persistComment.getUserId().equals(userId)
                && !persistComment.getPost().getUserId().equals(userId)) {
            throw new Exception("Not owner");
        }
        try {
            commentRepositoryJPA.delete(persistComment);
        } catch (Exception e) {
            return false;
        }
        applicationEventPublisher.publishEvent(
                new CommentTotalEvent(this, "commentTotalTopic", postId));
        if (persistComment.getParentComment() == null) {
            applicationEventPublisher.publishEvent(
                    new CommentEvent(this, "commentTopic", persistComment.getId(), postId, "DELETE"));
        } else {
            Comment parentComment = persistComment.getParentComment();
            parentComment = getReplyInfo(parentComment);
            applicationEventPublisher.publishEvent(
                    new CommentEvent(this, "commentTopic", parentComment.getId(), postId, "UPDATE"));
            applicationEventPublisher.publishEvent(
                    new ReplyEvent(this, "replyTopic", persistComment.getId(), parentComment.getId(), "DELETE"));
        }
        return true;
    }

    @Override
    public List<Comment> loadTwoComments(Long postId) {
        List<Comment> comments = commentRepositoryJPA.loadTwoComments(postId);
        comments.forEach(comment -> {
            comment.setRepliesTotal(commentRepositoryJPA.countReply(postId, comment.getId()));
            Comment firstReply = commentRepositoryJPA.getFirstReply(postId, comment.getId());
            if (firstReply != null) {
                firstReply.setUser(
                    webClientBuilder.build().get()
                        .uri("http://account-service/api/users",
                        uriBuilder -> uriBuilder.queryParam("userId", firstReply.getUserId()).build())
                        .retrieve()
                        .bodyToMono(User.class)
                        .block());
            }
            comment.setFirstReply(firstReply);
            User user = webClientBuilder.build().get()
                .uri("http://account-service/api/users",
                uriBuilder -> uriBuilder.queryParam("userId", comment.getUserId()).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
            comment.setUser(user);

            User repliedUser;
            if (comment.getRepliedUserId() != null) {
                repliedUser = webClientBuilder.build().get()
                    .uri("http://account-service/api/users",
                    uriBuilder -> uriBuilder.queryParam("userId", comment.getRepliedUserId()).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            } else {
                repliedUser = null;
            }
            comment.setRepliedUser(repliedUser);
        });
        return comments;
    }

    @Override
    public Comment create(Comment comment, Long postId, Long userId, Long commentId) throws Exception {
        Comment repliedComment = retrieve(commentId);
        Comment parentComment;
        Integer repliedLevel = commentRepositoryJPA.getLevelById(commentId);
        if (repliedLevel == 2) {
            parentComment = repliedComment.getParentComment();
            comment.setLevel(2);
        } else {
            parentComment = repliedComment;
            comment.setLevel(repliedLevel + 1);
        }
        comment.setRepliedUserId(repliedComment.getUserId());
        comment.setParentComment(parentComment);
        return create(comment, postId, userId);
    }

    @Override
    public List<Comment> loadComment(Long postId, Long commentId) {
        List<Comment> comments = commentRepositoryJPA.loadComment(postId, commentId);
        comments.forEach(
            comment -> {
                Comment firstReply = commentRepositoryJPA.getFirstReply(postId, comment.getId());
                if (firstReply != null) {
                    firstReply.setUser(
                        webClientBuilder.build().get()
                            .uri("http://account-service/api/users",
                            uriBuilder -> uriBuilder.queryParam("userId", firstReply.getUserId()).build())
                            .retrieve()
                            .bodyToMono(User.class)
                            .block());
                }
                comment.setFirstReply(firstReply);
                if (comment.getParentComment() != null) {
                    comment.setRepliedUser(
                        webClientBuilder.build().get()
                            .uri("http://account-service/api/users",
                            uriBuilder -> uriBuilder.queryParam("userId", comment.getRepliedUserId()).build())
                            .retrieve()
                            .bodyToMono(User.class)
                            .block()
                    );
                }
                comment.setUser(
                    webClientBuilder.build().get()
                        .uri("http://account-service/api/users",
                        uriBuilder -> uriBuilder.queryParam("userId", comment.getUserId()).build())
                        .retrieve()
                        .bodyToMono(User.class)
                        .block()
                );
            }
        );
        return comments;
    }

    @Override
    public Comment getReplyInfo(Comment comment) {
        comment.setRepliesTotal(commentRepositoryJPA.countReply(comment.getPostId(), comment.getId()));
        Comment firstReply = commentRepositoryJPA.getFirstReply(comment.getPostId(), comment.getId());
        if (firstReply != null) {
            firstReply.setUser(
                webClientBuilder.build().get()
                    .uri("http://account-service/api/users",
                    uriBuilder -> uriBuilder.queryParam("userId", firstReply.getUserId()).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block());
        }
        comment.setFirstReply(firstReply);
        return comment;
    }

    @Override
    public Comment getReplyInfo(Long commentId) throws Exception {
        Comment comment = retrieve(commentId);
        return getReplyInfo(comment);
    }
}
