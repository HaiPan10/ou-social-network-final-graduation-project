package com.ou.mailservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMailEvent {
    private String orderAction;
    private Long id;
    private String email;
    private String verificationCode;
    private String status;
    private String firstName;
    private String lastName;
}