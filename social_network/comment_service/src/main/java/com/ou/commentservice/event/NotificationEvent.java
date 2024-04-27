package com.ou.commentservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NotificationEvent extends ApplicationEvent {
    private String orderService;
    private String notificationType;
    private Long postId;
    private Long commentId;
    private Long parentCommentId;
    private Long reactionId;
    private String content; 
    private boolean isSeen;
    private Long currentUserId;
    private Long targetUserId;

    public NotificationEvent(Object source, String orderService, String notificationType,
    Long postId, Long commentId, String content, boolean isSeen, Long currentUserId, Long targetUserId) {
        super(source);
        this.orderService = orderService;
        this.notificationType = notificationType;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.isSeen = isSeen;
        this.currentUserId = currentUserId;
        this.targetUserId = targetUserId;
    }

    public NotificationEvent(Object source, String orderService, String notificationType,
    Long postId, Long commentId, Long parentCommentId, String content, boolean isSeen, 
    Long currentUserId, Long targetUserId) {
        super(source);
        this.orderService = orderService;
        this.notificationType = notificationType;
        this.postId = postId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.isSeen = isSeen;
        this.currentUserId = currentUserId;
        this.targetUserId = targetUserId;
    }

    public NotificationEvent(String orderService, String notificationType,
    Long postId, Long commentId, Long parentCommentId, String content, boolean isSeen, 
    Long currentUserId, Long targetUserId) {
        super(orderService);
        this.orderService = orderService;
        this.notificationType = notificationType;
        this.postId = postId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.isSeen = isSeen;
        this.currentUserId = currentUserId;
        this.targetUserId = targetUserId;
    }
}