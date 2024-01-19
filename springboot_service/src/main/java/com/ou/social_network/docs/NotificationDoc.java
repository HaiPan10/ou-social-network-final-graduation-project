package com.ou.social_network.docs;

import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDoc {
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
