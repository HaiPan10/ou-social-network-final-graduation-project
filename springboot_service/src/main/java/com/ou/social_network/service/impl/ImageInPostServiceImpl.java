package com.ou.social_network.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ou.social_network.pojo.ImageInPost;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.repository.repositoryJPA.ImageInPostRepositoryJPA;
import com.ou.social_network.service.interfaces.CloudinaryService;
import com.ou.social_network.service.interfaces.ImageInPostService;


@Service
public class ImageInPostServiceImpl implements ImageInPostService {
    // @Autowired
    // private ImageInPostRepository imageInPostRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageInPostRepositoryJPA imageInPostRepositoryJPA;

    @Override
    public List<ImageInPost> uploadImageInPost(List<MultipartFile> imageList, Post newPost) {
        List<String> imageUrl = imageList.parallelStream()
        .map(img -> {
            try {
                return cloudinaryService.uploadImage(img);
            } catch (IOException e) {
                try {
                    throw new Exception("Up ảnh thất bại");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        })
        .filter(url -> url != null)
        .collect(Collectors.toList());

        List<ImageInPost> imageInPosts = imageUrl.stream().map(img -> {
            ImageInPost imageInPost = new ImageInPost();
            imageInPost.setImageUrl(img);
            imageInPost.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            imageInPost.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            imageInPost.setPostId(newPost);
            return imageInPost;
        })
        .collect(Collectors.toList());
        return imageInPostRepositoryJPA.saveAll(imageInPosts);
        // return imageInPostRepository.uploadImage(imageInPosts);
    }

    @Override
    public void deleteImageInPost(List<ImageInPost> imageInPosts) {
        List<String> oldImageUrls = imageInPosts.stream().map(img -> img.getImageUrl()).collect(Collectors.toList());
        System.out.println(oldImageUrls);
        boolean isDeleted = true;
        try {
            imageInPostRepositoryJPA.deleteAll(imageInPosts);
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
    }
}

