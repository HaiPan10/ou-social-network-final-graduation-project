package com.ou.social_network.docs;

import com.google.cloud.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomDoc {
    private DocumentReference userDocRef;
    private DocumentReference chatRoomDocRef;
}
