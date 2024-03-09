package com.ou.accountservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderPlacedEvent extends ApplicationEvent {
    private String orderService;
    private String orderAction;

    public OrderPlacedEvent(Object source, String orderService, String orderAction) {
        super(source);
        this.orderService = orderService;
        this.orderAction = orderAction;
    }

    public OrderPlacedEvent(String orderService, String orderAction) {
        super(orderService);
        this.orderService = orderService;
        this.orderAction = orderAction;
    }
}