package com.ou.adminservice.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ou.adminservice.pojo.Post;


public interface PostService {
    Post uploadPost(String postContent, Long userId, List<MultipartFile> image, boolean isActiveComment) throws Exception;
    List<Post> loadPost(Long userId, Long currentUserId, Map<String, String> params) throws Exception;
    // List<Post> list(Map<String, String> params);
    boolean update(Post post, List<MultipartFile> images, boolean isEditImage) throws Exception;
    Post retrieve(Long postId) throws Exception;
    boolean delete(Long postId, Long userId) throws Exception;
    List<Post> loadNewFeed(Long currentUserId, @RequestParam Map<String, String> params) throws Exception;
    Long countPosts(Map<String, String> params);
    List<Post> search(Map<String, String> params);
    Post uploadPostSurvey(Post post, Long userId) throws Exception;
    Post uploadPostInvitation(Post post, Long userId) throws Exception;
    Object[][] stat(Map<String, String> params);
    Post getDetail(Long postId, Long userId) throws Exception;
}