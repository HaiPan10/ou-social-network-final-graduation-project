package com.ou.realtime_service.modals;

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
