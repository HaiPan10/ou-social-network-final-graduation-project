package com.ou.postservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PostEvent extends ApplicationEvent {
    private String orderService;
    private String orderAction;
    private Long postId;
    private String realtimeAction;

    public PostEvent(Object source, String orderService,
    String orderAction, Long postId, String realtimeAction) {
        super(source);
        this.orderService = orderService;
        this.orderAction = orderAction;
        this.postId = postId;
        this.realtimeAction = realtimeAction;
    }

    public PostEvent(String orderService, String orderAction, 
    Long postId, String realtimeAction) {
        super(orderService);
        this.orderService = orderService;
        this.orderAction = orderAction;
        this.postId = postId;
        this.realtimeAction = realtimeAction;
    }
}