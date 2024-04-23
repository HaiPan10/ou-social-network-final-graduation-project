package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.Date;

import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserFirebaseModal implements Serializable {
    private Long userId;
    private String displayName;
    private String photoUrl;
    private String activeStatus;
    @ServerTimestamp
    private Date updatedAt;

    public UserFirebaseModal(Long userId, String displayName, String photoUrl, String activeStatus) {
        this.userId = userId;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.activeStatus = activeStatus;
    }
}
