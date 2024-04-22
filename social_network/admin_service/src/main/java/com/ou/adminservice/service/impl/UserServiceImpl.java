package com.ou.adminservice.service.impl;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private MultipartBodyBuilder bodyBuilder;

    @Override
    public User create(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws IOException, Exception {

        return Mono.fromCallable(() -> {

            byte[] fileBytes = uploadAvatar.getBytes();

            bodyBuilder.part("file", new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return uploadAvatar.getOriginalFilename(); // Set the filename for the part
                }
            });

            return bodyBuilder.build();
        }).flatMap(body -> builder.build().post()
                .uri("http://account-service/api/users/upload_avatar", uriBuilder ->
                    uriBuilder.pathSegment("{userId}").build(userId))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchangeToMono(res -> {

                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(User.class);
                    }

                    return res.bodyToMono(String.class)
                            .flatMap(err -> Mono.error(new Exception(err)));
                }))
                .doOnError(err -> new Exception(err.getMessage()))
                .block();
    }
}