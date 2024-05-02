package com.ou.postservice.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ou.postservice.service.interfaces.CloudinaryService;
import com.ou.postservice.utils.CloudinaryUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class CloudinaryServiceImpl implements CloudinaryService{
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"))
        .get("secure_url").toString();
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {
        Map<String, String> response = cloudinary.uploader().destroy(CloudinaryUtils.getPublicId(imageUrl), ObjectUtils.emptyMap());
        System.out.println("Delete image: " + imageUrl + " status: " + response);
    }
}
