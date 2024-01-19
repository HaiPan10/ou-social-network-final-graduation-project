package com.ou.social_network.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ou.social_network.service.interfaces.CloudinaryService;
import com.ou.social_network.utils.CloudinaryUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class CloudinaryServiceImpl implements CloudinaryService{
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        System.out.println("[DEBUG] - CURRENTLY IN UPLOADIMAGE");
        return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"))
        .get("secure_url").toString();
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {
        System.out.println(CloudinaryUtils.getPublicId(imageUrl));
        Map<String, String> response = cloudinary.uploader().destroy(CloudinaryUtils.getPublicId(imageUrl), ObjectUtils.emptyMap());
        System.out.println(response);
    }
}
