package com.bb3.bodybuddybe.media.dto;

import com.bb3.bodybuddybe.media.entity.Media;
import com.bb3.bodybuddybe.media.enums.MediaTypeEnum;
import lombok.Getter;

@Getter
public class MediaDto {
    private Long id;
    private Long postId;
    private String s3Url;
    private MediaTypeEnum mediaType;

    public MediaDto(Media media) {
        this.id = media.getId();
        this.postId = media.getPost().getId();
        this.s3Url = media.getS3Url();
        this.mediaType = media.getMediaType();
    }
}
