package com.ou.social_network.service.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ou.social_network.pojo.ImageInPost;
import com.ou.social_network.pojo.Post;


public interface ImageInPostService {
    List<ImageInPost> uploadImageInPost(List<MultipartFile> imageList, Post newPost);
    void deleteImageInPost(List<ImageInPost> imageInPosts);
}
