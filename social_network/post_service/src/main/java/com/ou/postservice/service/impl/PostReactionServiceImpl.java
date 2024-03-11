package com.ou.postservice.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.postservice.event.OrderPlacedEvent;
import com.ou.postservice.pojo.NotificationFirebaseModal;
import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.PostReaction;
import com.ou.postservice.pojo.Reaction;
import com.ou.postservice.pojo.User;
import com.ou.postservice.repository.repositoryJPA.PostReactionRepositoryJPA;
import com.ou.postservice.repository.repositoryJPA.PostRepositoryJPA;
import com.ou.postservice.repository.repositoryJPA.ReactionRepositoryJPA;
// import com.ou.postservice.repository.repositoryJPA.UserRepositoryJPA;
// import com.ou.postservice.service.interfaces.FirebaseService;
// import com.ou.postservice.service.interfaces.SocketService;
import com.ou.postservice.service.interfaces.PostReactionService;
import jakarta.persistence.NoResultException;


@Service
@Transactional
public class PostReactionServiceImpl implements PostReactionService {

    @Autowired
    private PostReactionRepositoryJPA postReactionRepositoryJPA;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PostRepositoryJPA postRepositoryJPA;
    @Autowired
    private ReactionRepositoryJPA reactionRepositoryJPA;
    // @Autowired
    // private UserRepositoryJPA userRepositoryJPA;
    // @Autowired
    // private FirebaseService firebaseService;
    // @Autowired
    // private SocketService socketService;

    @Override
    public void countReaction(Post returnPost, Long currentUser) {
        // List<PostReaction> postReactions = postReactionRepository.countReaction(returnPost.getId());
        List<PostReaction> postReactions = postReactionRepositoryJPA.findByPostId(returnPost);
        Optional<PostReaction> reactionOptional = postReactions.stream().filter(p -> p.getUserId().equals(currentUser)).findFirst();
        if (reactionOptional.isPresent()) {
            returnPost.setCurrentReaction(reactionOptional.get().getReactionId());
        }
        returnPost.setReactionTotal(postReactions.stream()
        .collect(Collectors.groupingBy(postReaction -> postReaction.getReactionId().getId(), Collectors.counting())));
    }

    @Override
    public PostReaction reaction(Long postId, Long userId, Reaction reaction) throws Exception {
        // Reaction persistReaction = session.get(Reaction.class, reaction.getId());
        Reaction persistReaction = reactionRepositoryJPA.findById(reaction.getId()).get();

        Optional<Post> optionalPost = postRepositoryJPA.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new Exception("Post is unavailable!");
        }

        NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
        notificationDoc.setNotificationType("reaction");
        notificationDoc.setPostId(postId);
        notificationDoc.setReactionId(reaction.getId());
        notificationDoc.setContent(optionalPost.get().getContent());
        notificationDoc.setSeen(false);

        try {
            Optional<PostReaction> persistPostReaction = postReactionRepositoryJPA.findUserReaction(userId, postId);
            if(!persistPostReaction.isPresent()){
                throw new NoResultException();
            }
            persistPostReaction.get().setReactionId(persistReaction);
            persistPostReaction.get().setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            if (!optionalPost.get().getUserId().equals(userId)) {
                // firebaseService.notification(userId, optionalPost.get().getUserId(), notificationDoc);
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "notification"));
            }
            PostReaction postReaction = postReactionRepositoryJPA.save(persistPostReaction.get());
            // socketService.realtimePostReaction(postId, userId);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimePostReaction"));
            return postReaction;
        } catch (NoResultException e) {
            PostReaction postReaction = new PostReaction();
            postReaction.setPostId(optionalPost.get());
            postReaction.setUserId(userId);
            postReaction.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            postReaction.setReactionId(persistReaction);

            if (!optionalPost.get().getUserId().equals(userId)) {
                // firebaseService.notification(userId, optionalPost.get().getUserId(), notificationDoc);
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "notification"));
            }           
            PostReaction returnPostReaction = postReactionRepositoryJPA.save(postReaction);
            // socketService.realtimePostReaction(postId, userId);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimePostReaction"));
            return returnPostReaction;
        }
    }

    @Override
    public boolean delete(Long postId, Long userId) throws Exception {
        // return postReactionRepository.delete(postId, userId);
        try {
            postReactionRepositoryJPA.delete(postId, userId);
            // socketService.realtimePostReaction(postId, userId);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "realtimePostReaction"));
            return true;
        } catch (Exception e) {
            System.out.println("[DEBUG] - " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getReactionUsers(Long postId, Long reactionId) {
        // return postReactionRepository.getReactionUsers(postId, reactionId);
        return postReactionRepositoryJPA.getReactionUsers(postId, reactionId);
    }
    
}
