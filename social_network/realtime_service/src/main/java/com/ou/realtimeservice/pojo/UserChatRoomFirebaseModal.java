package com.ou.realtimeservice.pojo;

import com.google.cloud.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomFirebaseModal {
    private DocumentReference userDocRef;
    private DocumentReference chatRoomDocRef;
}
