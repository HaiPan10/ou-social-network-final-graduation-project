package com.ou.social_network.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.Comment;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.SocketClient;
import com.ou.social_network.service.interfaces.CommentService;
import com.ou.social_network.service.interfaces.FirebaseService;
import com.ou.social_network.service.interfaces.PostReactionService;
import com.ou.social_network.service.interfaces.SocketService;
import com.ou.social_network.socketModal.SocketCommentModal;
import com.ou.social_network.socketModal.SocketCommentTotalModal;
import com.ou.social_network.socketModal.SocketPostModal;
import com.ou.social_network.socketModal.SocketReactionModal;

@Service
public class SocketServiceImpl implements SocketService {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private CommentService commentService;

    private Map<String, List<SocketClient>> socketClients = new ConcurrentHashMap<>();
    private List<SocketClient> onlineClients = new ArrayList();

    @Override
    public void addSocketClient(String path, SocketClient socketClient) {
        List<SocketClient> clients = socketClients.getOrDefault(path, new ArrayList<>());
        clients.add(socketClient);
        socketClients.put(path, clients);
    }

    @Override
    public List<SocketClient> getSocketClientsForPath(String path) {
        return socketClients.getOrDefault(path, new ArrayList<>());
    }

    @Override
    public void removeSocketClient(String path, String sessionId) {
        List<SocketClient> clients = socketClients.getOrDefault(path, new ArrayList<>());
        SocketClient client = clients.stream().filter(c -> c.getName().equals(sessionId)).findFirst().orElse(null);
        if (client != null) {
            clients.remove(client);
            socketClients.put(path, clients);
        }
    }

    @Override
    public void addOnlineClient(SocketClient socketClient) throws InterruptedException, ExecutionException {
        onlineClients.add(socketClient);
        firebaseService.onlineUser(socketClient.getId().toString());
    }

    @Override
    public void removeOfflineClient(String sessionId) throws InterruptedException, ExecutionException {
        SocketClient client = onlineClients.stream().filter(c -> c.getName().equals(sessionId)).findFirst().orElse(null);
        if (client != null) {
            firebaseService.offlineUser(client.getId().toString());
        }
    }
    
    @Override
    public void realtimeComment(Comment comment, Long postId, String action) {
        System.out.println(" ACTION : " + action + " COMMENT: " + comment);
        commentService.getReplyInfo(comment);
        SocketCommentModal socketCommentModal = new SocketCommentModal(comment, action);
        template.convertAndSend(String.format("/comment/%s", postId), socketCommentModal);
    }


    @Override
    public void realtimePostReaction(Long postId, Long userId) {
        Post post = new Post(postId);
        postReactionService.countReaction(post, Long.valueOf(0));
        SocketReactionModal socketReactionModal = new SocketReactionModal(userId, post.getReactionTotal());
        template.convertAndSend(String.format("/reaction/%s", postId), socketReactionModal);
    }

    @Override
    public void realtimeCommentTotal(Long postId) {
        SocketCommentTotalModal socketCommentTotalModal = new SocketCommentTotalModal(
            commentService.countComment(postId), commentService.loadTwoComments(postId));
        template.convertAndSend(String.format("/comment-total/%s", postId), socketCommentTotalModal);
    }

    @Override
    public void realtimePost(SocketPostModal socketPostModal) {
        template.convertAndSend("/home", socketPostModal);
    }

    @Override
    public void realtimeProfile(SocketPostModal socketPostModal) {
        template.convertAndSend(String.format("/profile/%s", socketPostModal.getPost().getUserId().getId()), socketPostModal);
    }

    @Override
    public void realtimeReply(Comment comment, Long parentCommentId, String action) {
        SocketCommentModal socketCommentModal = new SocketCommentModal(comment, action);
        template.convertAndSend(String.format("/reply/%s", parentCommentId), socketCommentModal);
    }
}
