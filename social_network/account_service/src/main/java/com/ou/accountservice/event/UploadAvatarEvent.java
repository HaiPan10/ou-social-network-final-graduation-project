package com.ou.accountservice.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UploadAvatarEvent extends ApplicationEvent {
    private String orderService;
    private Long userId;
    private String newPhotoUrl;

    public UploadAvatarEvent(Object source, String orderService,
     Long userId, String newPhotoUrl) {
        super(source);
        this.orderService = orderService;
        this.userId = userId;
        this.newPhotoUrl = newPhotoUrl;
    }

    public UploadAvatarEvent(String orderService, Long userId, String newPhotoUrl) {
        super(orderService);
        this.orderService = orderService;
        this.userId = userId;
        this.newPhotoUrl = newPhotoUrl;
    }
}