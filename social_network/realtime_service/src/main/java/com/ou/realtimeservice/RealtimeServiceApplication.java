package com.ou.realtimeservice;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ou.realtimeservice.event.CommentEvent;
import com.ou.realtimeservice.event.CommentTotalEvent;
import com.ou.realtimeservice.event.NotificationEvent;
import com.ou.realtimeservice.event.ReplyEvent;
import com.ou.realtimeservice.event.UploadAvatarEvent;
import com.ou.realtimeservice.event.UserDocEvent;
import com.ou.realtimeservice.pojo.NotificationFirebaseModal;
import com.ou.realtimeservice.pojo.UserFirebaseModal;
import com.ou.realtimeservice.service.interfaces.FirebaseService;
import com.ou.realtimeservice.service.interfaces.SocketService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RealtimeServiceApplication {
	@Autowired
	private ObservationRegistry observationRegistry;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private SocketService socketService;

    public static void main(String[] args) {
		SpringApplication.run(RealtimeServiceApplication.class, args);
	}

    @KafkaListener(topics = "updateUserDocTopic")
    public void handleNotification(UserDocEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from updateUserDocTopic <id: {}, display name: {}, photourl: {}, active status: {}>",
            orderPlacedEvent.getUserId(), orderPlacedEvent.getDisplayName(), 
            orderPlacedEvent.getPhotoUrl(), orderPlacedEvent.getActiveStatus());
            UserFirebaseModal userFirebaseModal = new UserFirebaseModal(orderPlacedEvent.getUserId(),
            orderPlacedEvent.getDisplayName(), orderPlacedEvent.getPhotoUrl(), orderPlacedEvent.getActiveStatus());
            try {
                firebaseService.saveOrUpdate(userFirebaseModal);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @KafkaListener(topics = "uploadAvatarTopic")
    public void handleNotification(UploadAvatarEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from uploadAvatarTopic <id: {}, newPhotoUrl: {}>",
            orderPlacedEvent.getUserId(), orderPlacedEvent.getNewPhotoUrl());
            try {
                UserFirebaseModal userFirebaseModal = firebaseService.retrieve(orderPlacedEvent.getUserId().toString());
                userFirebaseModal.setPhotoUrl(orderPlacedEvent.getNewPhotoUrl());
                firebaseService.saveOrUpdate(userFirebaseModal);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        });
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(NotificationEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from notificationTopic <userId {} notify to userId {} with action {}>",
            orderPlacedEvent.getCurrentUserId(), orderPlacedEvent.getTargetUserId(), orderPlacedEvent.getNotificationType());
            try {
                NotificationFirebaseModal notificationFirebaseModal = new NotificationFirebaseModal(
                    orderPlacedEvent.getNotificationType(), orderPlacedEvent.getPostId(),
                    orderPlacedEvent.getReactionId(), orderPlacedEvent.getCommentId(), 
                    orderPlacedEvent.getParentCommentId(), orderPlacedEvent.getContent(), 
                    orderPlacedEvent.isSeen()
                );
                firebaseService.notification(orderPlacedEvent.getCurrentUserId(), orderPlacedEvent.getTargetUserId(), notificationFirebaseModal);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        });
    }

    @KafkaListener(topics = "commentTopic")
    public void handleNotification(CommentEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from commentTopic <action {} for commendId {} in postId {}>",
            orderPlacedEvent.getAction(), orderPlacedEvent.getCommentId(), orderPlacedEvent.getPostId());
            socketService.realtimeComment(orderPlacedEvent.getCommentId(), orderPlacedEvent.getPostId(), orderPlacedEvent.getAction());

        });
    }

    @KafkaListener(topics = "replyTopic")
    public void handleNotification(ReplyEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from replyTopic <action {} for parentCommentId {}>",
            orderPlacedEvent.getAction(), orderPlacedEvent.getParentCommentId());
            socketService.realtimeReply(orderPlacedEvent.getCommentId(), orderPlacedEvent.getParentCommentId(), orderPlacedEvent.getAction());

        });
    }

    @KafkaListener(topics = "commentTotalTopic")
    public void handleNotification(CommentTotalEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message from commentTotalTopic <postId {}>", orderPlacedEvent.getPostId());
            socketService.realtimeCommentTotal(orderPlacedEvent.getPostId());
        });
    }
	
	// @KafkaListener(topics = "realtimeTopic")
    // public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
    //     Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
    //         log.info("Got message <{}>", orderPlacedEvent.getOrderAction());
    //     });
    // }
}