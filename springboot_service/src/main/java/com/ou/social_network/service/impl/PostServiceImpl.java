package com.ou.social_network.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ou.social_network.docs.NotificationDoc;
import com.ou.social_network.pojo.ImageInPost;
import com.ou.social_network.pojo.InvitationGroup;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.PostInvitation;
import com.ou.social_network.pojo.PostInvitationUser;
import com.ou.social_network.pojo.PostSurvey;
import com.ou.social_network.pojo.Question;
import com.ou.social_network.pojo.User;
import com.ou.social_network.repository.interfaces.PostRepository;
import com.ou.social_network.repository.repositoryJPA.PostRepositoryJPA;
import com.ou.social_network.service.interfaces.CloudinaryService;
import com.ou.social_network.service.interfaces.CommentService;
import com.ou.social_network.service.interfaces.FirebaseService;
import com.ou.social_network.service.interfaces.GroupService;
import com.ou.social_network.service.interfaces.ImageInPostService;
import com.ou.social_network.service.interfaces.MailService;
import com.ou.social_network.service.interfaces.PostInvitationService;
import com.ou.social_network.service.interfaces.PostReactionService;
import com.ou.social_network.service.interfaces.PostService;
import com.ou.social_network.service.interfaces.PostSurveyService;
import com.ou.social_network.service.interfaces.QuestionService;
import com.ou.social_network.service.interfaces.SocketService;
import com.ou.social_network.service.interfaces.UserService;
import com.ou.social_network.socketModal.SocketPostModal;
import com.ou.social_network.utils.CloudinaryUtils;

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
    private CommentService commentService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PostSurveyService postSurveyService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostInvitationService postInvitationService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MailService mailService;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private Environment env;
    @Autowired
    private SocketService socketService;

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
        User persistUser = userService.retrieve(userId);
        newPost.setUserId(persistUser);
        postRepositoryJPA.save(newPost);
        if (images != null && images.get(0).getSize() != 0) {
            newPost.setImageInPostList(imageInPostService.uploadImageInPost(images, newPost));
        }
        postReactionService.countReaction(newPost, userId);
        newPost.setCommentTotal(commentService.countComment(newPost.getId()));

        if (userId.equals(Long.valueOf(1))) {
            NotificationDoc notificationDoc = new NotificationDoc();
            notificationDoc.setNotificationType("post");
            notificationDoc.setContent(postContent);
            notificationDoc.setPostId(newPost.getId());
            notificationDoc.setSeen(true);
            firebaseService.notification(userId, Long.valueOf(0), notificationDoc);
        }
        
        socketService.realtimePost(new SocketPostModal(newPost, "create"));
        socketService.realtimeProfile(new SocketPostModal(newPost, "create"));
        return newPost;
    }

    @Override
    public List<Post> loadPost(Long userId, Long currentUserId, Map<String, String> params) throws Exception {
        Optional<List<Post>> listPostOptional = postRepository.loadPost(userId, params, currentUserId);
        System.out.println("OUT POST REPOSITORY");
        if (listPostOptional.isPresent()) {
            List<Post> posts = listPostOptional.get();
            posts.forEach(p -> {
                postReactionService.countReaction(p, currentUserId);
                p.setCommentTotal(commentService.countComment(p.getId()));
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
        if (!persistPost.getUserId().getId().equals(post.getUserId().getId())) {
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
        if (!persistPost.getUserId().getId().equals(userId) && userId != 1) {
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
                p.setCommentTotal(commentService.countComment(p.getId()));
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
        User persistUser = userService.retrieve(userId);
        post.setUserId(persistUser);
        postRepositoryJPA.save(post);
        List<Question> questions = postSurvey.getQuestions();
        postSurvey.setQuestions(null);
        postSurvey = postSurveyService.uploadPostSurvey(post.getId(), postSurvey);
        questionService.create(postSurvey, questions);
        postSurvey.setQuestions(questions);
        post.setPostSurvey(postSurvey);

        NotificationDoc notificationDoc = new NotificationDoc();
        notificationDoc.setNotificationType("survey");
        notificationDoc.setContent(postSurvey.getSurveyTitle());
        notificationDoc.setPostId(post.getId());
        notificationDoc.setSeen(true);
        firebaseService.notification(userId, Long.valueOf(0), notificationDoc);

        postReactionService.countReaction(post, userId);
        post.setCommentTotal(commentService.countComment(post.getId()));

        socketService.realtimePost(new SocketPostModal(post, "create"));
        socketService.realtimeProfile(new SocketPostModal(post, "create"));
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
        User persistUser = userService.retrieve(userId);
        post.setUserId(persistUser);
        postRepositoryJPA.save(post);

        List<Long> listUserId = null;
        List<PostInvitationUser> list = postInvitation.getPostInvitationUsers();
        if (list != null) {
            if (list.size() > 0) {
                listUserId = list.stream()
                        .map(p -> Long.valueOf(p.getUserId().getId())).collect(Collectors.toList());
            } else {
                throw new Exception("Empty invitation user!");
            }
        }

        List<User> listUsers = userService.list(listUserId);
        postInvitation.setPostInvitationUsers(null);
        InvitationGroup group = postInvitation.getGroupId();
        postInvitation.setGroupId(null);
        postInvitation = postInvitationService.create(post.getId(), postInvitation, listUsers);

        if (group != null && listUsers != null) {
            group = groupService.create(group);
            groupService.addUsers(group.getId(), listUsers);
        }

        if (listUsers == null || listUsers.isEmpty()) {
            // fetch all user
            listUsers = userService.list();
        }

        postInvitation.setPost(post);

        final List<User> finalList = listUsers;
        final PostInvitation finalInvitation = postInvitation;

        Runnable runnable = () -> {
            mailService.sendMultipleEmail(finalList, finalInvitation);
        };

        executorService.execute(runnable);

        post.setPostInvitation(postInvitation);
        System.out.println("[DEBUG] - FINISH CALLING SEND MAIL AT " + Calendar.getInstance().getTimeInMillis());

        NotificationDoc notificationDoc = new NotificationDoc();
        notificationDoc.setNotificationType("invitation");
        notificationDoc.setPostId(post.getId());
        notificationDoc.setContent(postInvitation.getEventName());

        if (listUserId != null) {
            listUserId.forEach(id -> {
                try {
                    notificationDoc.setSeen(false);
                    firebaseService.notification(userId, id, notificationDoc);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } else {
            notificationDoc.setSeen(true);
            firebaseService.notification(userId, Long.valueOf(0), notificationDoc);
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
        post.setCommentTotal(commentService.countComment(postId));
        // post.setTwoComments(commentService.loadTwoComments(postId));
        post.getImageInPostList().forEach(img -> img
            .setContentType(String.format("image/%s", CloudinaryUtils.getImageType(img.getImageUrl()))));
        return post;
    }
}
