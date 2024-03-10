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

import com.ou.commentservice.pojo.NotificationFirebaseModal;
import com.cloudinary.provisioning.Account;
import com.ou.commentservice.event.OrderPlacedEvent;
import com.ou.commentservice.pojo.Comment;
import com.ou.commentservice.pojo.SocketClient;
import com.ou.commentservice.pojo.User;
import com.ou.commentservice.repository.repositoryJPA.CommentRepositoryJPA;
import com.ou.commentservice.service.interfaces.CommentService;
import com.ou.commentservice.pojo.Post;
// import com.ou.commentservice.service.interfaces.FirebaseService;
// import com.ou.commentservice.service.interfaces.PostService;
// import com.ou.commentservice.service.interfaces.SocketService;
// import com.ou.commentservice.service.interfaces.UserService;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepositoryJPA commentRepositoryJPA;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    // @Autowired
    // private PostService postService;
    // @Autowired
    // private UserService userService;
    // @Autowired
    // private FirebaseService firebaseService;
    // @Autowired
    // private SocketService socketService;

    @Override
    public Integer countComment(Long postId) {
        System.out.println("IN COMMENT SERVICE");
        return commentRepositoryJPA.countComment(postId);
    }

    @Override
    public Comment create(Comment comment, Long postId, Long userId) throws Exception {
        // User persistUser = userService.retrieve(userId);
        User persistUser = webClientBuilder.build().get()
            .uri("http://account-service/api/users",
            uriBuilder -> uriBuilder.queryParam("userId", userId).build())
            .retrieve()
            .bodyToMono(User.class)
            .block();
        // Post persistPost = postService.retrieve(postId);
        Post persistPost = webClientBuilder.build().get()
            .uri("http://account-service/api/posts",
            uriBuilder -> uriBuilder.queryParam("postId", postId).build())
            .retrieve()
            .bodyToMono(Post.class)
            .block();
        if (!persistPost.getIsActiveComment()) {
            throw new Exception("Bài đăng này bị khóa bình luận");
        } else {
            // return commentRepository.create(comment, persistPost, persistUser);
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setCreatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            comment.setUpdatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            commentRepositoryJPA.save(comment);

            // List<SocketClient> socketClients = socketService
            //         .getSocketClientsForPath(String.format("/comment/%s", postId));
            // boolean userIdExists = socketClients.stream()
            //         .anyMatch(client -> client.getId().equals(persistPost.getUserId().getId()));
            if (!persistPost.getUserId().getId().equals(userId)) {
                NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
                notificationDoc.setNotificationType("comment");
                notificationDoc.setCommentId(comment.getId());
                notificationDoc.setPostId(postId);
                notificationDoc.setContent(comment.getContent());
                notificationDoc.setSeen(false);
                // firebaseService.notification(userId, persistPost.getUserId().getId(), notificationDoc);
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "notification"));
            }

            if (comment.getParentComment() == null) {
                // socketService.realtimeComment(comment, postId, "CREATE");
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "realtimeComment"));
            } else {
                Comment parentComment = comment.getParentComment();
                parentComment = getReplyInfo(parentComment);
                // socketService.realtimeComment(parentComment, postId, "UPDATE");
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "realtimeComment"));
                // socketService.realtimeReply(comment, parentComment.getId(), "CREATE");
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "realtimeReply"));

                if (!comment.getUserId().equals(comment.getRepliedUser().getId())) {
                    NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
                    notificationDoc.setNotificationType("reply");
                    notificationDoc.setCommentId(comment.getId());
                    notificationDoc.setPostId(postId);
                    notificationDoc.setContent(comment.getContent());
                    notificationDoc.setSeen(false);
                    notificationDoc.setParentCommentId(comment.getParentComment().getId());
                    // firebaseService.notification(userId, comment.getRepliedUser().getId(), notificationDoc);
                    applicationEventPublisher.publishEvent(
                        new OrderPlacedEvent(this, "realtimeTopic", "notification"));
                }

            }
            // socketService.realtimeCommentTotal(postId);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeCommentTotal"));
            return comment;
        }
    }

    @Override
    public List<Comment> loadComment(Long postId) {
        List<Comment> comments = commentRepositoryJPA.loadComment(postId);
        comments.forEach(comment -> {
            comment.setRepliesTotal(commentRepositoryJPA.countReply(postId, comment.getId()));
            comment.setFirstReply(commentRepositoryJPA.getFirstReply(postId, comment.getId()));
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
            // socketService.realtimeComment(persistComment, persistComment.getPostId().getId(), "UPDATE");
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeComment"));
        } else {
            Comment parentComment = persistComment.getParentComment();
            // socketService.realtimeReply(persistComment, parentComment.getId(), "UPDATE");
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeReply"));
        }
        // socketService.realtimeCommentTotal(persistComment.getPostId().getId());
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimeCommentTotal"));

        return persistComment;
    }

    @Override
    public Comment retrieve(Long commentId) throws Exception {
        Optional<Comment> commentOptional = commentRepositoryJPA.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            User repliedUser = webClientBuilder.build().get()
                .uri("http://account-service/api/users",
                uriBuilder -> uriBuilder.queryParam("userId", comment.getRepliedUserId()).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
            comment.setRepliedUser(repliedUser);
            return comment;
        } else {
            throw new Exception("Không tìm thấy comment!");
        }
    }

    @Override
    public boolean delete(Long commentId, Long userId) throws Exception {
        Comment persistComment = retrieve(commentId);
        // Call service to fetch post
        Long postId = persistComment.getPostId();
        if (!persistComment.getUserId().equals(userId)
                && !persistComment.getPost().getUserId().getId().equals(userId)) {
            throw new Exception("Not owner");
        }
        try {
            commentRepositoryJPA.delete(persistComment);
        } catch (Exception e) {
            return false;
        }
        // socketService.realtimeCommentTotal(postId);
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimeCommentTotal"));
        if (persistComment.getParentComment() == null) {
            // socketService.realtimeComment(persistComment, postId, "DELETE");
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeComment"));
        } else {
            Comment parentComment = persistComment.getParentComment();
            parentComment = getReplyInfo(parentComment);
            // socketService.realtimeComment(parentComment, postId, "UPDATE");
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeComment"));
            // socketService.realtimeReply(persistComment, parentComment.getId(), "DELETE");
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimeReply"));
        }
        return true;
    }

    @Override
    public List<Comment> loadTwoComments(Long postId) {
        List<Comment> comments = commentRepositoryJPA.loadTwoComments(postId);
        comments.forEach(comment -> {
            comment.setRepliesTotal(commentRepositoryJPA.countReply(postId, comment.getId()));
            comment.setFirstReply(commentRepositoryJPA.getFirstReply(postId, comment.getId()));
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
        // User repliedUser = userService.retrieve(repliedComment.getUserId());
        comment.setRepliedUserId(repliedComment.getUserId());
        comment.setParentComment(parentComment);
        return create(comment, postId, userId);
    }

    @Override
    public List<Comment> loadComment(Long postId, Long commentId) {
        return commentRepositoryJPA.loadComment(postId, commentId);
    }

    @Override
    public Comment getReplyInfo(Comment comment) {
        comment.setRepliesTotal(commentRepositoryJPA.countReply(comment.getPostId(), comment.getId()));
        comment.setFirstReply(commentRepositoryJPA.getFirstReply(comment.getPostId(), comment.getId()));
        return comment;
    }
}
