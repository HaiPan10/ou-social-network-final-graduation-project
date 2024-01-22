package com.ou.realtime_service.service.interfaces;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ou.realtime_service.DTOs.Comment;
import com.ou.realtime_service.modals.SocketClient;
import com.ou.realtime_service.modals.PostSocketModal;

public interface SocketService {
    void addSocketClient(String path, SocketClient socketClient);
    List<SocketClient> getSocketClientsForPath(String path);
    void removeSocketClient(String path, String sessionId);
    void addOnlineClient(SocketClient socketClient) throws InterruptedException, ExecutionException;
    void removeOfflineClient(String sessionId) throws InterruptedException, ExecutionException;
    void realtimeComment(Comment comment, Long postId, String action);
    void realtimePostReaction(Long postId, Long userId);
    void realtimeCommentTotal(Long postId);
    void realtimePost(PostSocketModal socketPostModal);
    void realtimeProfile(PostSocketModal socketPostModal);
    void realtimeReply(Comment comment, Long parentCommentId, String action);
}
