package com.ou.accountservice.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ou.accountservice.event.AccountMailEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class AccountMailEventListener {
    @Autowired
    private KafkaTemplate<String, AccountMailEvent> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(AccountMailEvent event) {
        log.info("Order Placed Event Received, Sending AccountMailEvent to {}: {} {} {} {} {} {} {}",
         event.getOrderService(), event.getOrderAction(), event.getId(), event.getEmail(), event.getFirstName(),
         event.getLastName(), event.getVerificationCode(), event.getStatus());
        
        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted(event.getOrderService(), this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, AccountMailEvent>> future = kafkaTemplate.send(event.getOrderService(),
                        new AccountMailEvent(event.getOrderService(), event.getOrderAction(),
                        event.getId(), event.getEmail(), event.getVerificationCode(), event.getStatus(),
                        event.getFirstName(), event.getLastName()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}