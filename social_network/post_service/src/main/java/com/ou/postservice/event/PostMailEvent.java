package com.ou.postservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PostMailEvent extends ApplicationEvent {
    private String orderService;
    private String email;
    private String eventName;
    private String content;

    public PostMailEvent(Object source, String orderService, String email,
    String eventName, String content) {
        super(source);
        this.orderService = orderService;
        this.email = email;
        this.eventName = eventName;
        this.content = content;
    }

    public PostMailEvent(String orderService, String email,
    String eventName, String content) {
        super(orderService);
        this.orderService = orderService;
        this.email = email;
        this.eventName = eventName;
        this.content = content;
    }
}