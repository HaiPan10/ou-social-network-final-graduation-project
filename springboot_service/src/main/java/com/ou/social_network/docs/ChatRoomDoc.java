package com.ou.social_network.docs;

import java.io.Serializable;
import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDoc implements Serializable {
    private String roomName;
    private DocumentReference latestMessageDoc;
    private boolean isPrivate;
    @ServerTimestamp
    private Date updatedAt;
}
