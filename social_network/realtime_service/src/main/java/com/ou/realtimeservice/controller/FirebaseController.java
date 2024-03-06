package com.ou.realtimeservice.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.realtimeservice.configs.JwtService;
import com.ou.realtimeservice.pojo.MessageFirebaseModal;
import com.ou.realtimeservice.service.interfaces.FirebaseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/firebase")
public class FirebaseController {
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/chat_room/private/{targetUserId}")
    public ResponseEntity<?> sendFirstMessage(@PathVariable Long targetUserId, HttpServletRequest httpServletRequest, @RequestBody MessageFirebaseModal MessageFirebaseModal) throws InterruptedException, ExecutionException {
        Long accountId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(firebaseService.sendFirstMessage(targetUserId, accountId, MessageFirebaseModal));
    }

    @PostMapping("/message/{documentId}")
    public void sendNewMessage(@PathVariable String documentId, HttpServletRequest httpServletRequest, @RequestBody MessageFirebaseModal MessageFirebaseModal) throws InterruptedException, ExecutionException {
        Long accountId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
        firebaseService.sendNewMessage(documentId, accountId, MessageFirebaseModal);
    }

    @PutMapping("/message/seen/{documentId}")
    public void seenMessage(@PathVariable String documentId, HttpServletRequest httpServletRequest) throws InterruptedException, ExecutionException {
        Long accountId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
        firebaseService.seenMessage(documentId, accountId);
    }

    @PutMapping("/typing/in_typing/{documentId}")
    public void inTyping(@PathVariable String documentId, HttpServletRequest httpServletRequest) throws InterruptedException, ExecutionException {
        firebaseService.updateTyping(documentId, true);
    }

    @PutMapping("/typing/stop_typing/{documentId}")
    public void stopTyping(@PathVariable String documentId, HttpServletRequest httpServletRequest) throws InterruptedException, ExecutionException {
        firebaseService.updateTyping(documentId, false);
    }

    @PutMapping("/notification/seen/{documentId}")
    public void seenNotification(@PathVariable String documentId) throws InterruptedException, ExecutionException {
        firebaseService.seenNotification(documentId);
    }
}
