package com.ou.realtimeservice.service.impl;

import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.ou.realtimeservice.pojo.ChatRoomFirebaseModal;
import com.ou.realtimeservice.pojo.MessageFirebaseModal;
import com.ou.realtimeservice.pojo.UserFirebaseModal;
import com.ou.realtimeservice.service.interfaces.FirebaseService;
import com.ou.realtimeservice.pojo.UserChatRoomFirebaseModal;
import com.ou.realtimeservice.pojo.NotificationFirebaseModal;
import com.ou.realtimeservice.pojo.TypingFirebaseModal;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public String saveOrUpdate(UserFirebaseModal userDoc) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("user").document(userDoc.getUserId().toString()).set(userDoc);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public UserFirebaseModal retrieve(String documentId) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("user").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        UserFirebaseModal userDoc;
        if (documentSnapshot.exists()) {
            userDoc = documentSnapshot.toObject(UserFirebaseModal.class);
            return userDoc;
        }
        return null;
    }

    @Override
    public void delete(String documentId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("user").document(documentId).delete();
    }

    @Override
    public void onlineUser(String documentId) throws InterruptedException, ExecutionException {
        UserFirebaseModal userDoc = retrieve(documentId);
        if (userDoc != null) {
            userDoc.setUpdatedAt(null);
            userDoc.setActiveStatus("online");
            saveOrUpdate(userDoc);
        }
    }

    @Override
    public void offlineUser(String documentId) throws InterruptedException, ExecutionException {
        UserFirebaseModal userDoc = retrieve(documentId);
        if (userDoc != null) {
            userDoc.setUpdatedAt(null);
            userDoc.setActiveStatus("ofline");
            saveOrUpdate(userDoc);
        }
    }

    @Override
    public DocumentReference saveOrUpdate(ChatRoomFirebaseModal chatRoomDoc) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        chatRoomDoc.setUpdatedAt(null);
        dbFirestore.collection("chatRoom").document(chatRoomDoc.getRoomName()).set(chatRoomDoc);
        return getReference(chatRoomDoc.getRoomName(), "chatRoom");
    }

    @Override
    public DocumentReference getReference(String documentId, String collectionName) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection(collectionName).document(documentId);
    }

    @Override
    public void addUsers(UserChatRoomFirebaseModal userChatRoomDoc, String roomName, String userId) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("userChatRoom").document(String.format("user_%s_%s", userId, roomName)).set(userChatRoomDoc);
    }

    @Override
    public DocumentReference create(MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentReference> documentReferenceApiFuture = dbFirestore.collection("message").add(messageDoc);
        return documentReferenceApiFuture.get();
    }

    @Override
    public String sendFirstMessage(Long targetUserId, Long currentUserId, MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException {
        DocumentReference currentUserDocumentReference = getReference(currentUserId.toString(), "user");
        DocumentReference targetUserDocumentReference = getReference(targetUserId.toString(), "user");
        ChatRoomFirebaseModal chatRoomDoc = new ChatRoomFirebaseModal();
        chatRoomDoc.setPrivate(true);
        if (currentUserId < targetUserId) {
            chatRoomDoc.setRoomName(String.format("private_%s_%s", currentUserId, targetUserId));
        } else {
            chatRoomDoc.setRoomName(String.format("private_%s_%s", targetUserId, currentUserId));
        }
        DocumentReference chatRoomDocumentReference = saveOrUpdate(chatRoomDoc);
        addUsers(new UserChatRoomFirebaseModal(currentUserDocumentReference, chatRoomDocumentReference), chatRoomDoc.getRoomName(), currentUserId.toString());
        addUsers(new UserChatRoomFirebaseModal(targetUserDocumentReference, chatRoomDocumentReference), chatRoomDoc.getRoomName(), targetUserId.toString());
        addTyping(new TypingFirebaseModal(currentUserDocumentReference, chatRoomDocumentReference, false), chatRoomDoc.getRoomName(), currentUserId.toString());
        addTyping(new TypingFirebaseModal(targetUserDocumentReference, chatRoomDocumentReference, false), chatRoomDoc.getRoomName(), targetUserId.toString());
        messageDoc.setChatRoomDoc(chatRoomDocumentReference);
        messageDoc.setUserDoc(currentUserDocumentReference);
        messageDoc.setSeen(false);
        DocumentReference messageDocumentReference = create(messageDoc);
        chatRoomDoc.setLatestMessageDoc(messageDocumentReference);
        saveOrUpdate(chatRoomDoc);
        return chatRoomDoc.getRoomName();
    }

    @Override
    public void sendNewMessage(String documentId, Long userId, MessageFirebaseModal messageDoc) throws InterruptedException, ExecutionException {
        DocumentReference userDocumentReference = getReference(userId.toString(), "user");
        DocumentReference roomDocumentReference = getReference(documentId, "chatRoom");
        messageDoc.setChatRoomDoc(roomDocumentReference);
        messageDoc.setUserDoc(userDocumentReference);
        messageDoc.setSeen(false);
        DocumentReference messageDocumentReference = create(messageDoc);
        ApiFuture<DocumentSnapshot> future = roomDocumentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        ChatRoomFirebaseModal chatRoomDoc; 
        if (documentSnapshot.exists()) {
            chatRoomDoc = documentSnapshot.toObject(ChatRoomFirebaseModal.class);
            chatRoomDoc.setLatestMessageDoc(messageDocumentReference);
            saveOrUpdate(chatRoomDoc);
        }
    }

    @Override
    public void seenMessage(String documentId, Long accountId) throws InterruptedException, ExecutionException {
        DocumentReference messsageDocumentReference = getReference(documentId, "message");
        ApiFuture<DocumentSnapshot> future = messsageDocumentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        MessageFirebaseModal messageDoc;
        if (documentSnapshot.exists()) {
            messageDoc = documentSnapshot.toObject(MessageFirebaseModal.class);
            messageDoc.setSeen(true);
            messageDoc.setUpdatedAt(null);
            saveOrUpdate(messageDoc, documentId);
        }
    }

    @Override
    public DocumentReference saveOrUpdate(MessageFirebaseModal messageDoc, String documentId) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("message").document(documentId).set(messageDoc);
        return getReference(documentId, "message");
    }

    @Override
    public void addTyping(TypingFirebaseModal typingDoc, String roomName, String userId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("typing").document(String.format("typing_%s_%s", userId, roomName)).set(typingDoc);
    }

    @Override
    public void updateTyping(String documentId, boolean typingStatus) throws InterruptedException, ExecutionException {
        DocumentReference messsageDocumentReference = getReference(documentId, "typing");
        ApiFuture<DocumentSnapshot> future = messsageDocumentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        TypingFirebaseModal typingDoc;
        if (documentSnapshot.exists()) {
            typingDoc = documentSnapshot.toObject(TypingFirebaseModal.class);
            typingDoc.setTyping(typingStatus);
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection("typing").document(documentId).set(typingDoc);
        }
    }

    @Override
    public void notification(Long currentUserId, Long targetUserId, NotificationFirebaseModal notificationDoc) throws InterruptedException, ExecutionException {
        DocumentReference currentUserDocumentReference = getReference(currentUserId.toString(), "user");
        DocumentReference targetUserDocumentReference = getReference(targetUserId.toString(), "user");
        notificationDoc.setOwnerUserDocRef(targetUserDocumentReference);
        notificationDoc.setInteractUserDocRef(currentUserDocumentReference);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String documentName;
        if (notificationDoc.getNotificationType().equals("reaction")) {
            documentName = String.format("user_%s_interact_%s_reaction_%s", currentUserId, targetUserId, notificationDoc.getPostId());
        } else if (notificationDoc.getNotificationType().equals("comment")){
            documentName = String.format("user_%s_interact_%s_comment_%s", currentUserId, targetUserId, notificationDoc.getCommentId());
        } else if (notificationDoc.getNotificationType().equals("survey")){
            documentName = String.format("survey_%s", notificationDoc.getPostId());
        } else if (notificationDoc.getNotificationType().equals("invitation")) {
            documentName = String.format("invitation_%s_%s", notificationDoc.getPostId(), targetUserId);
        } else if (notificationDoc.getNotificationType().equals("reply")) {
            documentName = String.format("user_%s_reply_comment_%s", currentUserId, notificationDoc.getParentCommentId());
        } else {
            documentName = String.format("post_%s_%s", notificationDoc.getPostId(), targetUserId);
        }

        notificationDoc.setCreatedAt(null);
        dbFirestore.collection("notification").document(documentName).set(notificationDoc);
    }

    @Override
    public void seenNotification(String documentId) throws InterruptedException, ExecutionException {
        DocumentReference messsageDocumentReference = getReference(documentId, "notification");
        ApiFuture<DocumentSnapshot> future = messsageDocumentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        NotificationFirebaseModal notificationDoc;
        if (documentSnapshot.exists()) {
            notificationDoc = documentSnapshot.toObject(NotificationFirebaseModal.class);
            notificationDoc.setSeen(true);
            Firestore dbFirestore = FirestoreClient.getFirestore();
            dbFirestore.collection("notification").document(documentId).set(notificationDoc);
        }
    }
}
