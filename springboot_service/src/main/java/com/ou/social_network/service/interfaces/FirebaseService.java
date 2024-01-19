package com.ou.social_network.service.interfaces;

import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.ou.social_network.docs.ChatRoomDoc;
import com.ou.social_network.docs.MessageDoc;
import com.ou.social_network.docs.NotificationDoc;
import com.ou.social_network.docs.TypingDoc;
import com.ou.social_network.docs.UserChatRoomDoc;
import com.ou.social_network.docs.UserDoc;


public interface FirebaseService {
    String saveOrUpdate(UserDoc userDoc) throws InterruptedException, ExecutionException;
    UserDoc retrieve(String documentId) throws InterruptedException, ExecutionException;
    DocumentReference getReference(String documentId, String collectionName) throws InterruptedException, ExecutionException;
    void delete(String documentId) throws InterruptedException, ExecutionException;
    void onlineUser(String documentId) throws InterruptedException, ExecutionException;
    void offlineUser(String documentId) throws InterruptedException, ExecutionException;
    DocumentReference saveOrUpdate(ChatRoomDoc chatRoomDoc) throws InterruptedException, ExecutionException;
    DocumentReference create(MessageDoc messageDoc) throws InterruptedException, ExecutionException;
    void addUsers(UserChatRoomDoc userChatRoomDoc, String roomName, String userId) throws InterruptedException, ExecutionException;
    String sendFirstMessage(Long targetUserId, Long accountId, MessageDoc messageDoc) throws InterruptedException, ExecutionException;
    void sendNewMessage(String documentId, Long accountId, MessageDoc messageDoc) throws InterruptedException, ExecutionException;
    void seenMessage(String documentId, Long accountId) throws InterruptedException, ExecutionException;
    DocumentReference saveOrUpdate(MessageDoc messageDoc, String documentId) throws InterruptedException, ExecutionException;
    void addTyping(TypingDoc typingDoc, String roomName, String userId) throws InterruptedException, ExecutionException;
    void updateTyping(String documentId, boolean typingStatus) throws InterruptedException, ExecutionException;
    void notification(Long currentUserId, Long targetUserId, NotificationDoc notificationDoc) throws InterruptedException, ExecutionException;
    void seenNotification(String documentId) throws InterruptedException, ExecutionException;
}
