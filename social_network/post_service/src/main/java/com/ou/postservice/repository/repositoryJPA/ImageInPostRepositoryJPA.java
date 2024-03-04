package com.ou.postservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.postservice.pojo.ImageInPost;

public interface ImageInPostRepositoryJPA extends JpaRepository<ImageInPost, Long>{
    
}
