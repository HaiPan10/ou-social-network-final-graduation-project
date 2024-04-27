package com.ou.realtimeservice.pojo;

import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public NotificationFirebaseModal(String notificationType,
    Long postId, Long commentId, Long parentCommentId, 
    String content, boolean isSeen) {
        this.notificationType = notificationType;
        this.postId = postId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.isSeen = isSeen;
    }
}
