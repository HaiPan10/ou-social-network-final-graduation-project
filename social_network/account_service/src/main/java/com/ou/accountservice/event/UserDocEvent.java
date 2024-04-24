package com.ou.accountservice.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserDocEvent extends ApplicationEvent {
    private String orderService;
    private Long userId;
    private String displayName;
    private String photoUrl;
    private String activeStatus;

    public UserDocEvent(Object source, String orderService, Long userId,
     String displayName, String photoUrl, String activeStatus) {
        super(source);
        this.orderService = orderService;
        this.userId = userId;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.activeStatus = activeStatus;
    }

    public UserDocEvent(String orderService, Long userId,
     String displayName, String photoUrl, String activeStatus) {
        super(orderService);
        this.orderService = orderService;
        this.userId = userId;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.activeStatus = activeStatus;
    }
}