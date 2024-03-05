package com.ou.realtimeservice.service.interfaces;

import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.ou.realtimeservice.pojo.ChatRoomFirebaseModal;
import com.ou.realtimeservice.pojo.MessageFirebaseModal;
import com.ou.realtimeservice.pojo.UserFirebaseModal;
import com.ou.realtimeservice.pojo.UserChatRoomFirebaseModal;
import com.ou.realtimeservice.pojo.NotificationFirebaseModal;
import com.ou.realtimeservice.pojo.TypingFirebaseModal;

public interface FirebaseService {
    String saveOrUpdate(UserFirebaseModal userDoc) throws InterruptedException, ExecutionException;
    UserFirebaseModal retrieve(String documentId) throws InterruptedException, ExecutionException;
    DocumentReference getReference(String documentId, String collectionName) throws InterruptedException, ExecutionException;
    void delete(String documentId) throws InterruptedException, ExecutionException;
    void onlineUser(String documentId) throws InterruptedException, ExecutionException;
    void offlineUser(String documentId) throws InterruptedException, ExecutionException;
    DocumentReference saveOrUpdate(ChatRoomFirebaseModal chatRoomDoc) throws InterruptedException, ExecutionException;
    DocumentReference create(MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException;
    void addUsers(UserChatRoomFirebaseModal userChatRoomDoc, String roomName, String userId) throws InterruptedException, ExecutionException;
    String sendFirstMessage(Long targetUserId, Long accountId, MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException;
    void sendNewMessage(String documentId, Long accountId, MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException;
    void seenMessage(String documentId, Long accountId) throws InterruptedException, ExecutionException;
    DocumentReference saveOrUpdate(MessageFirebaseModal messageDoc, String documentId) throws InterruptedException, ExecutionException;
    void addTyping(TypingFirebaseModal typingDoc, String roomName, String userId) throws InterruptedException, ExecutionException;
    void updateTyping(String documentId, boolean typingStatus) throws InterruptedException, ExecutionException;
    void notification(Long currentUserId, Long targetUserId, NotificationFirebaseModal notificationDoc) throws InterruptedException, ExecutionException;
    void seenNotification(String documentId) throws InterruptedException, ExecutionException;
}