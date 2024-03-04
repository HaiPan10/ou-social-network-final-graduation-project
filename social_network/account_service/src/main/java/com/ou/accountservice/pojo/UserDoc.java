package com.ou.accountservice.pojo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDoc implements Serializable {
    private Long userId;
    private String displayName;
    private String photoUrl;
    private String activeStatus;
}
