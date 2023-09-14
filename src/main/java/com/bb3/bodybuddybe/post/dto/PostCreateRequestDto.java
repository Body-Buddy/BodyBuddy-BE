package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostCreateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요.")
    private CategoryEnum category;

    @NotNull(message = "헬스장 id를 입력해주세요.")
    private Long gymId;

    private List<MultipartFile> images;

    private List<MultipartFile> videos;
}
