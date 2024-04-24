package com.ou.accountservice.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AccountMailEvent extends ApplicationEvent {
    private String orderService;
    private String orderAction;
    private Long id;
    private String email;
    private String verificationCode;
    private String status;
    private String firstName;
    private String lastName;

    public AccountMailEvent(Object source, String orderService, String orderAction,
    Long id, String email, String verificationCode, String status,
    String firstName, String lastName) {
        super(source);
        this.orderService = orderService;
        this.orderAction = orderAction;
        this.id = id;
        this.email = email;
        this.verificationCode = verificationCode;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AccountMailEvent(String orderService, String orderAction,
    Long id, String email, String verificationCode, String status,
    String firstName, String lastName) {
        super(orderService);
        this.orderService = orderService;
        this.orderAction = orderAction;
        this.id = id;
        this.email = email;
        this.verificationCode = verificationCode;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}