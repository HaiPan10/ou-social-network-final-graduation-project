package com.ou.realtimeservice.pojo;

import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationFirebaseModal {
    private DocumentReference ownerUserDocRef;
    private DocumentReference interactUserDocRef;
    @ServerTimestamp
    private Date createdAt;
    private String notificationType;
    private Long postId;
    private Long commentId;
    private Long parentCommentId;
    private Long reactionId;
    private String content; 
    private boolean isSeen;
}
