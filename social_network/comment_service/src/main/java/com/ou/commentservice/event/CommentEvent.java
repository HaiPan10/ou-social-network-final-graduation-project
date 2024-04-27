package com.ou.commentservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentEvent extends ApplicationEvent {
    private String orderService;
    private Long commentId;
    private Long postId;
    private String action;

    public CommentEvent(Object source, String orderService, Long commentId,
     Long postId, String action) {
        super(source);
        this.orderService = orderService;
        this.commentId = commentId;
        this.postId = postId;
        this.action = action;
    }

    public CommentEvent(String orderService, Long commentId,
    Long postId, String action) {
        super(orderService);
        this.orderService = orderService;
        this.commentId = commentId;
        this.postId = postId;
        this.action = action;
    }
}