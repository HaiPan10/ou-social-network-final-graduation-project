package com.ou.realtimeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.ou.realtimeservice.pojo.OrderPlacedEvent;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RealtimeServiceApplication {
	@Autowired
	private ObservationRegistry observationRegistry;

    public static void main(String[] args) {
		SpringApplication.run(RealtimeServiceApplication.class, args);
	}

	
	@KafkaListener(topics = "realtimeTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent.getOrderAction());
        });
    }
}