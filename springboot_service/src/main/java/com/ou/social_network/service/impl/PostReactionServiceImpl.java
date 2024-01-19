package com.ou.social_network.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ou.social_network.docs.NotificationDoc;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.PostReaction;
import com.ou.social_network.pojo.Reaction;
import com.ou.social_network.pojo.User;
import com.ou.social_network.repository.repositoryJPA.PostReactionRepositoryJPA;
import com.ou.social_network.repository.repositoryJPA.PostRepositoryJPA;
import com.ou.social_network.repository.repositoryJPA.ReactionRepositoryJPA;
import com.ou.social_network.repository.repositoryJPA.UserRepositoryJPA;
import com.ou.social_network.service.interfaces.FirebaseService;
import com.ou.social_network.service.interfaces.PostReactionService;
import com.ou.social_network.service.interfaces.SocketService;
import jakarta.persistence.NoResultException;


@Service
@Transactional
public class PostReactionServiceImpl implements PostReactionService {

    @Autowired
    private PostReactionRepositoryJPA postReactionRepositoryJPA;
    @Autowired
    private PostRepositoryJPA postRepositoryJPA;
    @Autowired
    private UserRepositoryJPA userRepositoryJPA;
    @Autowired
    private ReactionRepositoryJPA reactionRepositoryJPA;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private SocketService socketService;

    @Override
    public void countReaction(Post returnPost, Long currentUser) {
        System.out.println("IN POST REACTION SERVICE");
        // List<PostReaction> postReactions = postReactionRepository.countReaction(returnPost.getId());
        List<PostReaction> postReactions = postReactionRepositoryJPA.findByPostId(returnPost);
        Optional<PostReaction> reactionOptional = postReactions.stream().filter(p -> p.getUserId().getId().equals(currentUser)).findFirst();
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

        NotificationDoc notificationDoc = new NotificationDoc();
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

            if (!optionalPost.get().getUserId().getId().equals(userId)) {
                firebaseService.notification(userId, optionalPost.get().getUserId().getId(), notificationDoc);
            }
            PostReaction postReaction = postReactionRepositoryJPA.save(persistPostReaction.get());
            socketService.realtimePostReaction(postId, userId);
            return postReaction;
        } catch (NoResultException e) {
            PostReaction postReaction = new PostReaction();
            postReaction.setPostId(optionalPost.get());
            Optional<User> optionalUser = userRepositoryJPA.findById(userId);
            if (optionalUser.isPresent()) {
                postReaction.setUserId(optionalUser.get());
            } else {
                throw new Exception("User is unavailable!");
            }
            postReaction.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            postReaction.setReactionId(persistReaction);

            if (!optionalPost.get().getUserId().getId().equals(userId)) {
                firebaseService.notification(userId, optionalPost.get().getUserId().getId(), notificationDoc);
            }           
            PostReaction returnPostReaction = postReactionRepositoryJPA.save(postReaction);
            socketService.realtimePostReaction(postId, userId);
            return returnPostReaction;
        }
    }

    @Override
    public boolean delete(Long postId, Long userId) throws Exception {
        // return postReactionRepository.delete(postId, userId);
        try {
            postReactionRepositoryJPA.delete(postId, userId);
            socketService.realtimePostReaction(postId, userId);
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
