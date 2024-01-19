package com.ou.social_network.docs;

import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDoc {
    private String content;
    private DocumentReference userDoc;
    private DocumentReference chatRoomDoc;
    @ServerTimestamp
    private Date createdAt;
    private boolean isSeen;
    @ServerTimestamp
    private Date updatedAt;
}
