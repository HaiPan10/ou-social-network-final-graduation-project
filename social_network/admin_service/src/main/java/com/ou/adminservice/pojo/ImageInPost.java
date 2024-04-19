package com.ou.adminservice.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;
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

    @Size(max = 300)
    private String imageUrl;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;

    @JsonIgnore
    private Post postId;

    private String contentType;

    @Override
    public String toString() {
        return "ImageInPost [id=" + id + ", imageUrl=" + imageUrl + ", contentType=" + contentType + "]";
    }
}
