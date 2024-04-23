package com.ou.realtimeservice;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ou.realtimeservice.event.OrderPlacedEvent;
import com.ou.realtimeservice.event.UploadAvatarEvent;
import com.ou.realtimeservice.event.UserDocEvent;
import com.ou.realtimeservice.pojo.UserFirebaseModal;
import com.ou.realtimeservice.service.interfaces.FirebaseService;

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
	
	@KafkaListener(topics = "realtimeTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent.getOrderAction());
        });
    }
}