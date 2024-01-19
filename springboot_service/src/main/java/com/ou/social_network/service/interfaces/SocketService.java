package com.ou.social_network.service.interfaces;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ou.social_network.pojo.Comment;
import com.ou.social_network.pojo.SocketClient;
import com.ou.social_network.socketModal.SocketPostModal;

public interface SocketService {
    void addSocketClient(String path, SocketClient socketClient);
    List<SocketClient> getSocketClientsForPath(String path);
    void removeSocketClient(String path, String sessionId);
    void addOnlineClient(SocketClient socketClient) throws InterruptedException, ExecutionException;
    void removeOfflineClient(String sessionId) throws InterruptedException, ExecutionException;
    void realtimeComment(Comment comment, Long postId, String action);
    void realtimePostReaction(Long postId, Long userId);
    void realtimeCommentTotal(Long postId);
    void realtimePost(SocketPostModal socketPostModal);
    void realtimeProfile(SocketPostModal socketPostModal);
    void realtimeReply(Comment comment, Long parentCommentId, String action);
}
