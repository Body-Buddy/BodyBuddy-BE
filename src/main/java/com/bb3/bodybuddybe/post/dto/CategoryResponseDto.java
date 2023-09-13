package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private String name;
    private String description;

    public CategoryResponseDto(CategoryEnum category) {
        this.name = category.name();
        this.description = category.getDescription();
    }
}
