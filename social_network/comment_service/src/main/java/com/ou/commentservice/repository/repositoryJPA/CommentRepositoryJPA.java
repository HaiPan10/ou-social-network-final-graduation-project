package com.ou.commentservice.repository.repositoryJPA;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ou.commentservice.pojo.Comment;

public interface CommentRepositoryJPA extends JpaRepository<Comment, Long> {
       @Query("SELECT Count(*) FROM Comment WHERE postId = ?1")
       Integer countComment(Long postId);

       @Query("SELECT c " +
                     "FROM Comment c " +
                     "WHERE c.postId = ?1 AND c.level = 1 " +
                     "ORDER BY c.createdDate DESC")
       List<Comment> loadComment(Long postId);

       @Query("SELECT c " +
                     "FROM Comment c " +
                     "WHERE c.postId = ?1 AND c.level = 1 " +
                     "ORDER BY c.createdDate DESC " +
                     "LIMIT 2")
       List<Comment> loadTwoComments(Long postId);

       @Query("SELECT c " +
                     "FROM Comment c " +
                     "WHERE c.postId = ?1 AND c.parentComment.id = ?2 " +
                     "ORDER BY c.createdDate ASC")
       List<Comment> loadComment(Long postId, Long commentId);

       @Query("SELECT Count (c.id) " +
                     "FROM Comment c " +
                     "WHERE c.postId = ?1 AND c.parentComment.id = ?2 ")
       Long countReply(Long postId, Long commentId);

       @Query("SELECT c " +
                     "FROM Comment c " +
                     "WHERE c.postId = ?1 AND c.parentComment.id = ?2 " +
                     "ORDER BY c.createdDate ASC " + 
                     "LIMIT 1")
       Comment getFirstReply(Long postId, Long commentId);

       @Query("SELECT c.level FROM Comment c where c.id = :id")
       Integer getLevelById(@Param("id") Long id);
}
