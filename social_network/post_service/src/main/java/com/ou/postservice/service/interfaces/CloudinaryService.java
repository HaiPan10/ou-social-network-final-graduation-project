package com.ou.postservice.service.interfaces;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile image) throws IOException;
    void deleteImage(String imageUrl) throws IOException;
}
