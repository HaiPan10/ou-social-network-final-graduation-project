package com.ou.realtimeservice.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageInPost implements Serializable {
    private Long id;
    private String imageUrl;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;
    private Post postId;
    private String contentType;

    @Override
    public String toString() {
        return "ImageInPost [id=" + id + ", imageUrl=" + imageUrl + ", contentType=" + contentType + "]";
    }
}
