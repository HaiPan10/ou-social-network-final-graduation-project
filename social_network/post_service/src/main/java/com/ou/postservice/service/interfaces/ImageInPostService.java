package com.ou.postservice.service.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ou.postservice.pojo.ImageInPost;
import com.ou.postservice.pojo.Post;


public interface ImageInPostService {
    List<ImageInPost> uploadImageInPost(List<MultipartFile> imageList, Post newPost);
    void deleteImageInPost(List<ImageInPost> imageInPosts);
}
