package com.ou.realtime_service.modals;

import com.google.cloud.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypingFirebaseModal {
    private DocumentReference userDocRef;
    private DocumentReference chatRoomDocRef;
    private boolean isTyping;
}
