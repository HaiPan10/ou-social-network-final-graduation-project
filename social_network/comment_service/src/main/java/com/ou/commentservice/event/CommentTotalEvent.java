package com.ou.commentservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentTotalEvent extends ApplicationEvent {
    private String orderService;
    private Long postId;

    public CommentTotalEvent(Object source, String orderService, Long postId) {
        super(source);
        this.orderService = orderService;
        this.postId = postId;
    }

    public CommentTotalEvent(String orderService, Long postId) {
        super(orderService);
        this.orderService = orderService;
        this.postId = postId;
    }
}