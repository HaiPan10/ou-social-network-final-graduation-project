package com.ou.postservice.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.postservice.pojo.NotificationFirebaseModal;
import com.ou.postservice.event.OrderPlacedEvent;
import com.ou.postservice.pojo.Account;
import com.ou.postservice.pojo.ImageInPost;
import com.ou.postservice.pojo.InvitationGroup;
import com.ou.postservice.pojo.NotificationFirebaseModal;
import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.PostInvitation;
import com.ou.postservice.pojo.PostInvitationUser;
import com.ou.postservice.pojo.PostSurvey;
import com.ou.postservice.pojo.Question;
import com.ou.postservice.pojo.User;
import com.ou.postservice.repository.interfaces.PostRepository;
import com.ou.postservice.repository.repositoryJPA.PostRepositoryJPA;
import com.ou.postservice.service.interfaces.CloudinaryService;
import com.ou.postservice.service.interfaces.ImageInPostService;
import com.ou.postservice.service.interfaces.PostInvitationService;
import com.ou.postservice.service.interfaces.PostReactionService;
import com.ou.postservice.service.interfaces.PostService;
import com.ou.postservice.service.interfaces.PostSurveyService;
import com.ou.postservice.service.interfaces.QuestionService;
import com.ou.postservice.utils.CloudinaryUtils;

// import com.ou.postservice.service.interfaces.SocketService;
// import com.ou.postservice.service.interfaces.UserService;
// import com.ou.postservice.pojo.SocketPostModal;
// import com.ou.postservice.service.interfaces.CommentService;
// import com.ou.postservice.service.interfaces.FirebaseService;
// import com.ou.postservice.service.interfaces.GroupService;
// import com.ou.postservice.service.interfaces.MailService;

@Service
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostRepositoryJPA postRepositoryJPA;
    @Autowired
    @Qualifier("executorService")
    private ExecutorService executorService;
    @Autowired
    private ImageInPostService imageInPostService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PostSurveyService postSurveyService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PostInvitationService postInvitationService;
    @Autowired
    private Environment env;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    // @Autowired
    // private SocketService socketService;
    // @Autowired
    // private GroupService groupService;
    // @Autowired
    // private MailService mailService;
    // @Autowired
    // private FirebaseService firebaseService;
    // @Autowired
    // private UserService userService;
    // @Autowired
    // private CommentService commentService;

    @Override
    public Post uploadPost(String postContent, Long userId, List<MultipartFile> images, boolean isActiveComment)
            throws Exception {
        Post newPost = new Post();
        if (postContent.length() == 0 && (images == null || images.get(0).getSize() == 0)) {
            throw new Exception("Empty post!");
        }
        newPost.setContent(postContent);
        newPost.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        newPost.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        newPost.setIsActiveComment(isActiveComment);
        newPost.setUserId(userId);
        postRepositoryJPA.save(newPost);
        if (images != null && images.get(0).getSize() != 0) {
            newPost.setImageInPostList(imageInPostService.uploadImageInPost(images, newPost));
        }
        postReactionService.countReaction(newPost, userId);

        Integer commentTotal = webClientBuilder.build().get()
            .uri("http://comment-service/api/comments/count",
            uriBuilder -> uriBuilder.queryParam("postId", newPost.getId()).build())
            .retrieve()
            .bodyToMono(Integer.class)
            .block();
        newPost.setCommentTotal(commentTotal);

        if (userId.equals(Long.valueOf(1))) {
            NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
            notificationDoc.setNotificationType("post");
            notificationDoc.setContent(postContent);
            notificationDoc.setPostId(newPost.getId());
            notificationDoc.setSeen(true);
            // firebaseService.notification(userId, Long.valueOf(0), notificationDoc);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "notification"));
        }
        
        // socketService.realtimePost(new SocketPostModal(newPost, "create"));
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimePost"));
        // socketService.realtimeProfile(new SocketPostModal(newPost, "create"));
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimeProfile"));
        return newPost;
    }

    @Override
    public Map<String, Object> loadProfile(Long userId, Long currentUserId, Map<String, String> params) throws Exception {
        // Account retrieveAccount = accountService.retrieve(userId);
        Account retrieveAccount = webClientBuilder.build().get()
        .uri("http://account-service/api/accounts/retrieve",
            uriBuilder -> uriBuilder.queryParam("accountId", userId).build())
            .retrieve()
            .bodyToMono(Account.class)
            .block();
        if (!(retrieveAccount.getStatus().equals("ACTIVE") || retrieveAccount.getStatus().equals("PASSWORD_CHANGE_REQUIRED"))) {
            throw new Exception("Not activated Account!");
        }
        System.out.println("DEBUG ACC: " + retrieveAccount);
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("user", retrieveAccount.getUser());
        jsonObject.put("role", retrieveAccount.getRoleId());
        jsonObject.put("posts", loadPost(userId, currentUserId, params));
        return jsonObject;
    }

    @Override
    public List<Post> loadPost(Long userId, Long currentUserId, Map<String, String> params) throws Exception {
        Optional<List<Post>> listPostOptional = postRepository.loadPost(userId, params, currentUserId);
        System.out.println("DEBUG: " + listPostOptional.get().size());
        if (listPostOptional.isPresent()) {
            List<Post> posts = listPostOptional.get();
            posts.forEach(p -> {
                postReactionService.countReaction(p, currentUserId);
                Integer commentTotal = webClientBuilder.build().get()
                    .uri("http://comment-service/api/comments/count",
                    uriBuilder -> uriBuilder.queryParam("postId", p.getId()).build())
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
                p.setCommentTotal(commentTotal);
                // p.setTwoComments(commentService.loadTwoComments(p.getId()));
                p.getImageInPostList().forEach(img -> img
                        .setContentType(String.format("image/%s", CloudinaryUtils.getImageType(img.getImageUrl()))));
            });
            return posts;
        } else {
            throw new Exception("User không hợp lệ!");
        }
    }

    @Override
    public boolean update(Post post, List<MultipartFile> images, boolean isEditImage) throws Exception {
        Post persistPost = retrieve(post.getId());
        if (!persistPost.getUserId().equals(post.getUserId())) {
            throw new Exception("Not owner");
        } else if (isEditImage) {
            List<ImageInPost> imageInPostList = persistPost.getImageInPostList();
            if (imageInPostList.size() != 0) {
                imageInPostService.deleteImageInPost(imageInPostList);
            }
            if (images != null) {
                persistPost.setImageInPostList(imageInPostService.uploadImageInPost(images, persistPost));
            } else {
                imageInPostList.clear();
                persistPost.setImageInPostList(imageInPostList);
            }
        }

        try {
            if (post.getContent() != null) {
                persistPost.setContent(post.getContent());
            }
            if (post.getIsActiveComment() != null) {
                persistPost.setIsActiveComment(post.getIsActiveComment());
            }
            persistPost.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            postRepositoryJPA.save(persistPost);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Post retrieve(Long postId) throws Exception {
        System.out.println("[DEBUG] - INSIDE THE POST SERVICE");
        // Optional<Post> postOptional = postRepository.retrieve(postId);
        Optional<Post> postOptional = postRepositoryJPA.findById(postId);
        if (postOptional.isPresent()) {
            return postOptional.get();
        } else {
            throw new Exception("Không tìm thấy bài đăng!");
        }
    }

    @Override
    public synchronized boolean delete(Long postId, Long userId) throws Exception {
        Post persistPost = retrieve(postId);
        if (!persistPost.getUserId().equals(userId) && userId != 1) {
            throw new Exception("Not owner");
        }
        List<String> oldImageUrls = persistPost.getImageInPostList().stream().map(img -> img.getImageUrl())
                .collect(Collectors.toList());
        System.out.println("GOT PERSIST: " + persistPost);
        boolean isDeleted = true;
        try {
            postRepositoryJPA.delete(persistPost);
        } catch (Exception e) {
            isDeleted = false;
        }
        if (isDeleted) {
            oldImageUrls.forEach(oldImage -> {
                try {
                    cloudinaryService.deleteImage(oldImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return true;
    }

    @Override
    public List<Post> loadNewFeed(Long currentUserId, @RequestParam Map<String, String> params) throws Exception {
        Optional<List<Post>> listPostOptional = postRepository.loadNewFeed(currentUserId, params);
        if (listPostOptional.isPresent() && listPostOptional.get().size() != 0) {
            List<Post> posts = listPostOptional.get();

            posts.forEach(p -> {
                postReactionService.countReaction(p, currentUserId);
                Integer commentTotal = webClientBuilder.build().get()
                    .uri("http://comment-service/api/comments/count",
                    uriBuilder -> uriBuilder.queryParam("postId", p.getId()).build())
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
                p.setCommentTotal(commentTotal);
                User user = webClientBuilder.build().get()
                    .uri("http://account-service/api/users",
                    uriBuilder -> uriBuilder.queryParam("userId", p.getUserId()).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
                p.setUser(user);
                // p.setTwoComments(commentService.loadTwoComments(p.getId()));
                p.getImageInPostList().forEach(img -> img
                        .setContentType(String.format("image/%s", CloudinaryUtils.getImageType(img.getImageUrl()))));
            });
            return posts;
        } else {
            throw new Exception("No more post");
        }
    }

    // @Override
    // public List<Post> list(Map<String, String> params) {
    // return postRepository.list(params);
    // }

    @Override
    public Long countPosts(Map<String, String> params) {
        String kw;
        if (params != null) {

            kw = params.get("kw");
            if (kw == null || kw.isEmpty()) {
                kw = "_";
            } else {
                kw = kw.trim();
            }

        } else {
            kw = "_";
        }
        return postRepositoryJPA.countPosts(kw);
    }

    @Override
    public List<Post> search(Map<String, String> params) {
        int page;
        String kw;
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p) - 1;
            } else {
                page = 0;
            }

            kw = params.get("kw");
            if (kw == null || kw.isEmpty()) {
                kw = "_";
            } else {
                kw = kw.trim();
            }

        } else {
            page = 0;
            kw = "_";
        }
        int pageSize = Integer.parseInt(this.env.getProperty("PENDING_ACCOUNT_PAGE_SIZE"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepositoryJPA.search(kw, pageable);
    }

    @Override
    public Post uploadPostSurvey(Post post, Long userId) throws Exception {
        if (post.getPostSurvey().getQuestions() == null || post.getPostSurvey().getQuestions().size() == 0) {
            throw new Exception("Empty question!");
        }
        post.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        post.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        PostSurvey postSurvey = post.getPostSurvey();
        post.setPostSurvey(null);
        // postRepository.uploadPost(post, userId);
        post.setUserId(userId);
        postRepositoryJPA.save(post);
        List<Question> questions = postSurvey.getQuestions();
        postSurvey.setQuestions(null);
        postSurvey = postSurveyService.uploadPostSurvey(post.getId(), postSurvey);
        questionService.create(postSurvey, questions);
        postSurvey.setQuestions(questions);
        post.setPostSurvey(postSurvey);

        NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
        notificationDoc.setNotificationType("survey");
        notificationDoc.setContent(postSurvey.getSurveyTitle());
        notificationDoc.setPostId(post.getId());
        notificationDoc.setSeen(true);
        // firebaseService.notification(userId, Long.valueOf(0), notificationDoc);
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "notification"));

        postReactionService.countReaction(post, userId);
        Integer commentTotal = webClientBuilder.build().get()
            .uri("http://comment-service/api/comments/count",
            uriBuilder -> uriBuilder.queryParam("postId", post.getId()).build())
            .retrieve()
            .bodyToMono(Integer.class)
            .block();
        post.setCommentTotal(commentTotal);

        // socketService.realtimePost(new SocketPostModal(post, "create"));
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimePost"));
        // socketService.realtimeProfile(new SocketPostModal(post, "create"));
        applicationEventPublisher.publishEvent(
            new OrderPlacedEvent(this, "realtimeTopic", "realtimeProfile"));
        return post;
    }

    @Override
    public Post uploadPostInvitation(Post post, Long userId) throws Exception {
        System.out.println("[DEBUG] - START COUNT TIME " + Calendar.getInstance().getTimeInMillis());
        post.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        post.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        PostInvitation postInvitation = post.getPostInvitation();
        post.setPostInvitation(null);
        // postRepository.uploadPost(post, userId);
        post.setUserId(userId);
        postRepositoryJPA.save(post);

        List<Long> listUserId;
        List<PostInvitationUser> list = postInvitation.getPostInvitationUsers();
        if (list != null) {
            if (list.size() > 0) {
                listUserId = list.stream()
                        .map(p -> Long.valueOf(p.getUserId())).collect(Collectors.toList());
            } else {
                throw new Exception("Empty invitation user!");
            }
        } else {
            throw new Exception("Null invitation user list!");
        }

        // List<User> listUsers = userService.list(listUserId);
        List<User> listUsers = webClientBuilder.build().get()
            .uri("http://account-service/api/users/list",
            uriBuilder -> uriBuilder.queryParam("listUserId", listUserId).build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<User>>() {})
            .block();

        postInvitation.setPostInvitationUsers(null);
        Long groupId = postInvitation.getGroupId().getId();

        // InvitationGroup group = postInvitation.getGroupId();
        InvitationGroup group = webClientBuilder.build().get()
            .uri("http://account-service/api/groups",
            uriBuilder -> uriBuilder.queryParam("invitationGroupId", groupId).build())
            .retrieve()
            .bodyToMono(InvitationGroup.class)
            .block();
        // postInvitation.setGroupId(null);
        postInvitation = postInvitationService.create(post.getId(), postInvitation, listUsers);

        if (group != null && listUsers != null) {
            // Hải handle this
            // group = groupService.create(group);
            // groupService.addUsers(group.getId(), listUsers);
        }

        if (listUsers == null || listUsers.isEmpty()) {
            // fetch all user
            // Hải handle this
            // listUsers = userService.list();
        }

        postInvitation.setPost(post);

        final List<User> finalList = listUsers;
        final PostInvitation finalInvitation = postInvitation;

        Runnable runnable = () -> {
            // mailService.sendMultipleEmail(finalList, finalInvitation);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "mailTopic", "sendMultipleEmail"));
        };

        executorService.execute(runnable);

        post.setPostInvitation(postInvitation);
        System.out.println("[DEBUG] - FINISH CALLING SEND MAIL AT " + Calendar.getInstance().getTimeInMillis());

        NotificationFirebaseModal notificationDoc = new NotificationFirebaseModal();
        notificationDoc.setNotificationType("invitation");
        notificationDoc.setPostId(post.getId());
        notificationDoc.setContent(postInvitation.getEventName());

        if (listUserId != null) {
            listUserId.forEach(id -> {
                notificationDoc.setSeen(false);
                // firebaseService.notification(userId, id, notificationDoc);
                applicationEventPublisher.publishEvent(
                    new OrderPlacedEvent(this, "realtimeTopic", "notification"));
            });
        } else {
            notificationDoc.setSeen(true);
            // firebaseService.notification(userId, Long.valueOf(0), notificationDoc);
            applicationEventPublisher.publishEvent(
                new OrderPlacedEvent(this, "realtimeTopic", "notification"));
        }

        // socketService.realtimePost(new SocketPostModal(post, "create"));

        return post;
    }

    @Override
    public List<Object[]> stat(Map<String, String> params) {
        boolean byMonth = params.get("byMonth") != null ? true : false;
        boolean byQuarter = params.get("byQuarter") != null ? true : false;
        Integer year = params.get("year") != null ? Integer.valueOf(params.get("year")) : null;
        return postRepositoryJPA.stat(byMonth, byQuarter, year);
    }

    @Override
    public Post getDetail(Long postId, Long userId) throws Exception {
        Post post = retrieve(postId);
        postReactionService.countReaction(post, userId);
        Integer commentTotal = webClientBuilder.build().get()
            .uri("http://comment-service/api/comments/count",
            uriBuilder -> uriBuilder.queryParam("postId", postId).build())
            .retrieve()
            .bodyToMono(Integer.class)
            .block();
        post.setCommentTotal(commentTotal);
        // post.setTwoComments(commentService.loadTwoComments(postId));
        post.getImageInPostList().forEach(img -> img
            .setContentType(String.format("image/%s", CloudinaryUtils.getImageType(img.getImageUrl()))));
        User user = webClientBuilder.build().get()
            .uri("http://account-service/api/users",
            uriBuilder -> uriBuilder.queryParam("userId", userId).build())
            .retrieve()
            .bodyToMono(User.class)
            .block();
        post.setUser(user);
        return post;
    }
}
