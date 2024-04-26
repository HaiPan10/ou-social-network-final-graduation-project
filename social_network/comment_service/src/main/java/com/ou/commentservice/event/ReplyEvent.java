package com.ou.commentservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ReplyEvent extends ApplicationEvent {
    private String orderService;
    private Long commentId;
    private Long parentCommentId;
    private String action;

    public ReplyEvent(Object source, String orderService, Long commentId,
     Long parentCommentId, String action) {
        super(source);
        this.orderService = orderService;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.action = action;
    }

    public ReplyEvent(String orderService, Long commentId,
    Long parentCommentId, String action) {
        super(orderService);
        this.orderService = orderService;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.action = action;
    }
}