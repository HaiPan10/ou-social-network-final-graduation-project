package com.ou.postservice.repository.repositoryJPA;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.PostReaction;
import com.ou.postservice.pojo.User;

public interface PostReactionRepositoryJPA extends JpaRepository<PostReaction, Long>{
    List<PostReaction> findByPostId(Post postId);
    @Query("SELECT pr " +
           "FROM PostReaction pr " +
           "WHERE pr.userId = :userId AND pr.postId.id = :postId")
    Optional<PostReaction> findUserReaction(@Param("userId") Long userId, @Param("postId") Long postId);
  
    @Modifying
    @Query("DELETE FROM PostReaction pr " +
           "WHERE pr.userId = :userId AND pr.postId.id = :postId")
    void delete(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("SELECT p.userId FROM PostReaction p WHERE p.reactionId.id = :reactionId AND p.postId.id = :postId")
    List<User> getReactionUsers(@Param("postId") Long postId, @Param("reactionId") Long reactionId);
}
