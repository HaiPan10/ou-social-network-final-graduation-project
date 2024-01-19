package com.ou.social_network.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ou.social_network.pojo.Comment;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.PostInvitation;
import com.ou.social_network.pojo.PostInvitationUser;
import com.ou.social_network.pojo.PostSurvey;
import com.ou.social_network.repository.interfaces.PostRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;



@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Environment env;

    @Override
    public Optional<List<Post>> loadPost(Long userId, @RequestParam Map<String, String> params, Long currentUserId) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);

        Root<Post> rPost = criteriaQuery.from(Post.class);
        List<Predicate> predicates = new ArrayList<>();
        rPost.fetch("postSurvey", JoinType.LEFT).fetch("questions", JoinType.LEFT);

        Join<Post, PostInvitation> joinPostInvitation = rPost.join("postInvitation", JoinType.LEFT);
        Join<PostInvitation, PostInvitationUser> joinPostInvitationUser = joinPostInvitation.join("postInvitationUsers", JoinType.LEFT);

        // Join<Post, PostSurvey> joinPostSurvey = rPost.join("postSurvey", JoinType.LEFT);
        // Join<PostSurvey, Response> joinResponse = joinPostSurvey.join("responses", JoinType.LEFT);

        System.out.println("PREDICATES INVITATION");
        predicates.add(builder.or(
            builder.isNull(joinPostInvitation),
            builder.isNull(joinPostInvitationUser),
            builder.equal(joinPostInvitationUser.get("userId").as(Long.class), currentUserId)
        ));

        // System.out.println("startAt" + joinPostInvitation.get("startAt"));
        // System.out.println("TODAY " + new Date());
        // predicates.add(builder.and(
        //     builder.or(
        //         builder.isNull(joinPostSurvey),
        //         builder.isNull(joinResponse),
        //         builder.notEqual(joinResponse.get("userId"), currentUserId)
        //     ),
        //     builder.or(
        //         builder.isNull(joinPostInvitation),
        //         builder.isNull(joinPostInvitationUser),
        //         builder.equal(joinPostInvitationUser.get("userId"), currentUserId)
        //         // builder.and(
        //         //     builder.equal(joinPostInvitationUser.get("userId"), currentUserId)
        //         //     builder.greaterThan(joinPostInvitation.get("startAt"), new Date())
        //         // )
        //     )
        // ));

        System.out.println("PREDICATES POST");
        predicates.add(builder.equal(rPost.get("userId").as(Long.class), userId));
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(rPost.get("createdAt")));

        Query query = session.createQuery(criteriaQuery);

        int page;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p);
            } else {
                page = 1;
            }
        } else {
            page = 1;
        }
        int pageSize = Integer.parseInt(this.env.getProperty("POST_PAGE_SIZE"));

        System.out.println("DONE PAGESIZE");
        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);

        try {
            List<Post> posts = query.getResultList();
            List<Post> filteredPosts = posts.stream()
                .filter(p -> !isResponse(p.getId(), currentUserId))
                .collect(Collectors.toList());
            filteredPosts.forEach(post -> {
                try {
                    Hibernate.initialize(post.getPostInvitation().getPostInvitationUsers());
                } catch (NullPointerException e) {
                    // Handle the exception if needed
                }
            });

            return Optional.of(filteredPosts);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    

    @Override
    public Optional<List<Post>> loadNewFeed(Long currentUserId, @RequestParam Map<String, String> params) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);

        Root<Post> rPost = criteriaQuery.from(Post.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Post, PostInvitation> joinPostInvitation = rPost.join("postInvitation", JoinType.LEFT);
        Join<PostInvitation, PostInvitationUser> joinPostInvitationUser = joinPostInvitation.join("postInvitationUsers", JoinType.LEFT);

        // Join<Post, PostSurvey> joinPostSurvey = rPost.join("postSurvey", JoinType.LEFT);
        // Join<PostSurvey, Response> joinResponse = joinPostSurvey.join("responses");
        rPost.fetch("postSurvey", JoinType.LEFT).fetch("questions", JoinType.LEFT);
        // System.out.println("startAt" + joinPostInvitation.get("startAt"));
        // System.out.println("TODAY " + new Date());

        predicates.add(builder.or(
            builder.isNull(joinPostInvitation),
            builder.isNull(joinPostInvitationUser),
            builder.equal(joinPostInvitationUser.get("userId").as(Long.class), currentUserId)
        ));

        // predicates.add(builder.and(
        //     builder.or(
        //         builder.isNull(joinPostSurvey),
        //         builder.isNull(joinResponse),
        //         builder.notEqual(joinResponse.get("userId"), currentUserId)
        //     ),
        //     builder.or(
        //         builder.isNull(joinPostInvitation),
        //         builder.isNull(joinPostInvitationUser),
        //         builder.equal(joinPostInvitationUser.get("userId"), currentUserId)
        //         // builder.and(
        //         //     builder.equal(joinPostInvitationUser.get("userId"), currentUserId)
        //         //     builder.greaterThan(joinPostInvitation.get("startAt"), new Date())
        //         // )
        //     )
        // ));

        Subquery<Date> subquery = criteriaQuery.subquery(Date.class);
        Root<Comment> rComment = subquery.from(Comment.class);
        List<Predicate> subPredicates = new ArrayList<>();
        subPredicates.add(builder.equal(rComment.get("postId").as(Long.class), rPost.get("id")));
        subquery.where(subPredicates.toArray(Predicate[]::new));
        subquery.select((Expression) builder.max(rComment.get("updatedDate")));

        criteriaQuery.orderBy(
                builder.desc(
                        builder.coalesce(
                                subquery,
                                rPost.get("createdAt"))),
                builder.desc(rPost.get("createdAt")));

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        Query query = session.createQuery(criteriaQuery);

        int page;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p);
            } else {
                page = 1;
            }
        } else {
            page = 1;
        }
        int pageSize = Integer.parseInt(this.env.getProperty("POST_PAGE_SIZE"));

        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);

        try {
            List<Post> posts = query.getResultList();
            List<Post> filteredPosts = posts.stream()
                .filter(p -> !isResponse(p.getId(), currentUserId))
                .collect(Collectors.toList());
            filteredPosts.forEach(post -> {
                try {
                    Hibernate.initialize(post.getPostInvitation().getPostInvitationUsers());
                } catch (NullPointerException e) {
                    // Handle the exception if needed
                }
            });
            return Optional.of(filteredPosts);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isResponse(Long postId, Long userId) {
        Session session = entityManager.unwrap(Session.class);
        try {
            Query query = session.createQuery("SELECT ps FROM PostSurvey ps WHERE ps.id = :postId AND :userId IN (SELECT r.userId.id FROM Response r WHERE r.surveyId = ps)");
            query.setParameter("postId", postId);
            query.setParameter("userId", userId);
            PostSurvey postSurvey = (PostSurvey) query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
