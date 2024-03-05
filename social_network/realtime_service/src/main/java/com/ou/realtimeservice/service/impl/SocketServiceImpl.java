package com.ou.realtimeservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ou.realtimeservice.pojo.Comment;
import com.ou.realtimeservice.pojo.Post;
import com.ou.realtimeservice.pojo.SocketClient;
// import com.ou.realtimeservice.service.interfaces.CommentService;
import com.ou.realtimeservice.service.interfaces.FirebaseService;
// import com.ou.realtimeservice.service.interfaces.PostReactionService;
import com.ou.realtimeservice.service.interfaces.SocketService;
import com.ou.realtimeservice.pojo.CommentSocketModal;
import com.ou.realtimeservice.pojo.CommentTotalSocketModal;
import com.ou.realtimeservice.pojo.PostSocketModal;
import com.ou.realtimeservice.pojo.ReactionSocketModal;

@Service
public class SocketServiceImpl implements SocketService {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FirebaseService firebaseService;
    // @Autowired
    // private PostReactionService postReactionService;
    // @Autowired
    // private CommentService commentService;

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
        // commentService.getReplyInfo(comment);
        CommentSocketModal socketCommentModal = new CommentSocketModal(comment, action);
        template.convertAndSend(String.format("/comment/%s", postId), socketCommentModal);
    }


    @Override
    public void realtimePostReaction(Long postId, Long userId) {
        Post post = new Post(postId);
        // postReactionService.countReaction(post, Long.valueOf(0));
        ReactionSocketModal socketReactionModal = new ReactionSocketModal(userId, post.getReactionTotal());
        template.convertAndSend(String.format("/reaction/%s", postId), socketReactionModal);
    }

    @Override
    public void realtimeCommentTotal(Long postId) {
        // CommentTotalSocketModal socketCommentTotalModal = new CommentTotalSocketModal(
            // commentService.countComment(postId), commentService.loadTwoComments(postId));
        // template.convertAndSend(String.format("/comment-total/%s", postId), socketCommentTotalModal);
    }

    @Override
    public void realtimePost(PostSocketModal socketPostModal) {
        template.convertAndSend("/home", socketPostModal);
    }

    @Override
    public void realtimeProfile(PostSocketModal socketPostModal) {
        template.convertAndSend(String.format("/profile/%s", socketPostModal.getPost().getUserId().getId()), socketPostModal);
    }

    @Override
    public void realtimeReply(Comment comment, Long parentCommentId, String action) {
        CommentSocketModal socketCommentModal = new CommentSocketModal(comment, action);
        template.convertAndSend(String.format("/reply/%s", parentCommentId), socketCommentModal);
    }
}
