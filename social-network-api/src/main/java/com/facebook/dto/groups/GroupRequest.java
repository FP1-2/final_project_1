package com.facebook.dto.groups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 28, message = "The name should not exceed 28 characters.")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 145, message = "The description must not exceed 145 characters.")
    private String description;

    private String imageUrl;
}
