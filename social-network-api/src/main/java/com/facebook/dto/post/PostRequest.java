package com.facebook.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Post image is mandatory.")
    private String imageUrl;

    @NotBlank(message = "The post title cannot be empty.")
    @Size(max = 200, message = "The title of the post should not exceed 200 characters.")
    private String title;

    @NotBlank(message = "The text of the post cannot be empty")
    private String body;

}
