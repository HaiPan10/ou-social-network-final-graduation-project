package com.ou.realtimeservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    @Autowired
    private WebClient.Builder webClientBuilder;

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
        Comment newComment = webClientBuilder.build().get()
            .uri("http://comment-service/api/comments/getReplyInfo",
            uriBuilder -> uriBuilder.queryParam("comment", comment).build())
            .retrieve()
            .bodyToMono(Comment.class)
            .block();
        CommentSocketModal socketCommentModal = new CommentSocketModal(newComment, action);
        template.convertAndSend(String.format("/comment/%s", postId), socketCommentModal);
    }


    @Override
    public void realtimePostReaction(Long postId, Long userId) {
        Post newPost = webClientBuilder.build().get()
            .uri("http://post-service/api/post_reactions/countReaction",
                uriBuilder -> uriBuilder.queryParam("postId", postId).build())
            .retrieve()
            .bodyToMono(Post.class)
            .block();
        ReactionSocketModal socketReactionModal = new ReactionSocketModal(userId, newPost.getReactionTotal());
        template.convertAndSend(String.format("/reaction/%s", postId), socketReactionModal);
    }

    @Override
    public void realtimeCommentTotal(Long postId) {
        CommentTotalSocketModal socketCommentTotalModal = new CommentTotalSocketModal(
            webClientBuilder.build().get()
                .uri("http://comment-service/api/comments/count",
                uriBuilder -> uriBuilder.queryParam("postId", postId).build())
                .retrieve()
                .bodyToMono(Integer.class)
                .block(),
            webClientBuilder.build().get()
                .uri("http://comment-service/api/comments/loadTwoComments",
                uriBuilder -> uriBuilder.queryParam("postId", postId).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Comment>>() {})
                .block());
        template.convertAndSend(String.format("/comment-total/%s", postId), socketCommentTotalModal);
    }

    @Override
    public void realtimePost(PostSocketModal socketPostModal) {
        template.convertAndSend("/home", socketPostModal);
    }

    @Override
    public void realtimeProfile(PostSocketModal socketPostModal) {
        template.convertAndSend(String.format("/profile/%s", socketPostModal.getPost().getUserId()), socketPostModal);
    }

    @Override
    public void realtimeReply(Comment comment, Long parentCommentId, String action) {
        CommentSocketModal socketCommentModal = new CommentSocketModal(comment, action);
        template.convertAndSend(String.format("/reply/%s", parentCommentId), socketCommentModal);
    }
}
