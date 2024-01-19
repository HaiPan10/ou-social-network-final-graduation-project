package com.ou.social_network.docs;

import java.io.Serializable;
import java.util.Date;

import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDoc implements Serializable {
    private Long userId;
    private String displayName;
    private String photoUrl;
    private String activeStatus;
    @ServerTimestamp
    private Date updatedAt;
}
