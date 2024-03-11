package com.ou.social_network.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.docs.NotificationDoc;
import com.ou.social_network.pojo.Comment;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.User;
import com.ou.social_network.repository.repositoryJPA.CommentRepositoryJPA;
import com.ou.social_network.service.interfaces.CommentService;
import com.ou.social_network.service.interfaces.FirebaseService;
import com.ou.social_network.service.interfaces.PostService;
import com.ou.social_network.service.interfaces.SocketService;
import com.ou.social_network.service.interfaces.UserService;

@Service
public class CommentServiceImpl implements CommentService {
    // @Autowired
    // private CommentRepository commentRepository;
    @Autowired
    private CommentRepositoryJPA commentRepositoryJPA;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private SocketService socketService;

    @Override
    public Integer countComment(Long postId) {
        System.out.println("IN COMMENT SERVICE");
        return commentRepositoryJPA.countComment(postId);
    }

    @Override
    public Comment create(Comment comment, Long postId, Long userId) throws Exception {
        User persistUser = userService.retrieve(userId);
        Post persistPost = postService.retrieve(postId);
        if (!persistPost.getIsActiveComment()) {
            throw new Exception("Bài đăng này bị khóa bình luận");
        } else {
            // return commentRepository.create(comment, persistPost, persistUser);
            comment.setPostId(persistPost);
            comment.setUserId(persistUser);
            comment.setCreatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            comment.setUpdatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            commentRepositoryJPA.save(comment);

            // List<SocketClient> socketClients = socketService
            //         .getSocketClientsForPath(String.format("/comment/%s", postId));
            // boolean userIdExists = socketClients.stream()
            //         .anyMatch(client -> client.getId().equals(persistPost.getUserId().getId()));
            if (!persistPost.getUserId().getId().equals(userId)) {
                NotificationDoc notificationDoc = new NotificationDoc();
                notificationDoc.setNotificationType("comment");
                notificationDoc.setCommentId(comment.getId());
                notificationDoc.setPostId(postId);
                notificationDoc.setContent(comment.getContent());
                notificationDoc.setSeen(false);
                firebaseService.notification(userId, persistPost.getUserId().getId(), notificationDoc);
            }

            if (comment.getParentComment() == null) {
                socketService.realtimeComment(comment, postId, "CREATE");
            } else {
                Comment parentComment = comment.getParentComment();
                parentComment = getReplyInfo(parentComment);
                socketService.realtimeComment(parentComment, postId, "UPDATE");
                socketService.realtimeReply(comment, parentComment.getId(), "CREATE");

                if (!comment.getUserId().getId().equals(comment.getRepliedUser().getId())) {
                    NotificationDoc notificationDoc = new NotificationDoc();
                    notificationDoc.setNotificationType("reply");
                    notificationDoc.setCommentId(comment.getId());
                    notificationDoc.setPostId(postId);
                    notificationDoc.setContent(comment.getContent());
                    notificationDoc.setSeen(false);
                    notificationDoc.setParentCommentId(comment.getParentComment().getId());
                    firebaseService.notification(userId, comment.getRepliedUser().getId(), notificationDoc);
                }

            }
            socketService.realtimeCommentTotal(postId);
            return comment;
        }
    }

    @Override
    public List<Comment> loadComment(Long postId) {
        // return commentRepository.loadComment(postId);
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
        if (!persistComment.getUserId().getId().equals(comment.getUserId().getId())) {
            throw new Exception("Not owner");
        }
        persistComment.setContent(comment.getContent());
        persistComment.setUpdatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        commentRepositoryJPA.save(persistComment);
        // socketService.realtimeComment(persistComment,
        // persistComment.getPostId().getId(), "UPDATE");
        if (persistComment.getParentComment() == null) {
            socketService.realtimeComment(persistComment, persistComment.getPostId().getId(), "UPDATE");
        } else {
            Comment parentComment = persistComment.getParentComment();
            // parentComment = getReplyInfo(parentComment);
            // socketService.realtimeComment(parentComment, postId, "CREATE");
            socketService.realtimeReply(persistComment, parentComment.getId(), "UPDATE");
        }
        socketService.realtimeCommentTotal(persistComment.getPostId().getId());

        return persistComment;
    }

    @Override
    public Comment retrieve(Long commentId) throws Exception {
        Optional<Comment> commentOptional = commentRepositoryJPA.findById(commentId);
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        } else {
            throw new Exception("Không tìm thấy comment!");
        }
    }

    @Override
    public boolean delete(Long commentId, Long userId) throws Exception {
        Comment persistComment = retrieve(commentId);
        Long postId = persistComment.getPostId().getId();
        if (!persistComment.getUserId().getId().equals(userId)
                && !persistComment.getPostId().getUserId().getId().equals(userId)) {
            throw new Exception("Not owner");
        }
        try {
            commentRepositoryJPA.delete(persistComment);
        } catch (Exception e) {
            return false;
        }
        socketService.realtimeCommentTotal(postId);
        // socketService.realtimeComment(persistComment, postId, "DELETE");
        if (persistComment.getParentComment() == null) {
            socketService.realtimeComment(persistComment, postId, "DELETE");
        } else {
            Comment parentComment = persistComment.getParentComment();
            parentComment = getReplyInfo(parentComment);
            socketService.realtimeComment(parentComment, postId, "UPDATE");
            socketService.realtimeReply(persistComment, parentComment.getId(), "DELETE");
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
        comment.setRepliedUser(repliedComment.getUserId());
        comment.setParentComment(parentComment);
        return create(comment, postId, userId);
    }

    @Override
    public List<Comment> loadComment(Long postId, Long commentId) {
        return commentRepositoryJPA.loadComment(postId, commentId);
    }

    @Override
    public Comment getReplyInfo(Comment comment) {
        comment.setRepliesTotal(commentRepositoryJPA.countReply(comment.getPostId().getId(), comment.getId()));
        comment.setFirstReply(commentRepositoryJPA.getFirstReply(comment.getPostId().getId(), comment.getId()));
        return comment;
    }
}
