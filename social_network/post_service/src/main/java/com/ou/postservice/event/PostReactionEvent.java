package com.ou.postservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PostReactionEvent extends ApplicationEvent {
    private String orderService;
    private Long postId;
    private Long userId;

    public PostReactionEvent(Object source, String orderService,
    Long postId, Long userId) {
        super(source);
        this.orderService = orderService;
        this.postId = postId;
        this.userId = userId;
    }

    public PostReactionEvent(String orderService, 
    Long postId, Long userId) {
        super(orderService);
        this.orderService = orderService;
        this.postId = postId;
        this.userId = userId;
    }
}