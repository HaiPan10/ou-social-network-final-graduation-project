package com.ou.adminservice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.Post;
import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.PostService;

import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Post uploadPost(String postContent, Long userId, List<MultipartFile> image, boolean isActiveComment)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadPost'");
    }

    @Override
    public List<Post> loadPost(Long userId, Long currentUserId, Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadPost'");
    }

    @Override
    public boolean update(Post post, List<MultipartFile> images, boolean isEditImage) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Post retrieve(Long postId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }

    @Override
    public boolean delete(Long postId, Long userId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Post> loadNewFeed(Long currentUserId, Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadNewFeed'");
    }

    @Override
    public Long countPosts(Map<String, String> params) {
        return builder.build().get()
                .uri("http://post-service/api/posts/count",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("kw", Optional.ofNullable(params.get("kw")))
                                .build())
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    @Override
    public List<Post> search(Map<String, String> params) {
        return builder.build().get()
                .uri("http://post-service/api/posts/search",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("page", Optional.ofNullable(params.get("page")))
                                .queryParamIfPresent("kw", Optional.ofNullable(params.get("kw")))
                                .queryParamIfPresent("status", Optional.ofNullable(params.get("status")))
                                .build())
                .retrieve()
                .bodyToFlux(Post.class)
                .flatMap(post -> {
                    return builder.build().get()
                        .uri("http://account-service/api/users",
                            uriBuilder -> uriBuilder.queryParam("userId", post.getUserId()).build())
                        .retrieve()
                        .bodyToMono(User.class)
                        .map(user -> {
                            post.setUser(user);
                            return post;
                        });
                })
                .collect(Collectors.toList())
                .onErrorResume(err -> Mono.empty())
                .block();
    }

    @Override
    public Post uploadPostSurvey(Post post, Long userId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadPostSurvey'");
    }

    @Override
    public Post uploadPostInvitation(Post post, Long userId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadPostInvitation'");
    }

    @Override
    public Object[][] stat(Map<String, String> params) {
        return builder.build().get()
                .uri("http://post-service/api/posts/stat/post",
                        uriBuilder -> uriBuilder
                                .queryParamIfPresent("year", Optional.ofNullable(params.get("year")))
                                .queryParamIfPresent("byMonth", Optional.ofNullable(params.get("byMonth")))
                                .queryParamIfPresent("byQuarter", Optional.ofNullable(params.get("byQuarter")))
                                .build())
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

    @Override
    public Post getDetail(Long postId, Long userId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDetail'");
    }

}
