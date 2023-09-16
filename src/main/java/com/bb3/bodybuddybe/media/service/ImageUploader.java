package com.bb3.bodybuddybe.media.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploader {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile file) {
        String fileName = generateFileName(file.getOriginalFilename());
        try (InputStream fileInputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, fileInputStream, metadata);
            s3Client.putObject(putObjectRequest);

            return s3Client.getUrl(bucketName, fileName).toString();

        } catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD_FILE);
        }
    }

    private String generateFileName(String originalName) {
        return UUID.randomUUID().toString() + "_" + originalName;
    }

    public void deleteFromUrl(String s3Url) {
        String fileName = extractFileNameFromUrl(s3Url);
        delete(fileName);
    }

    private String extractFileNameFromUrl(String s3Url) {
        if (!s3Url.contains(bucketName)) {
            log.error("Invalid S3 URL: {}", s3Url);
            throw new CustomException(ErrorCode.INVALID_S3_URL);
        }

        // 버킷 이름 다음의 문자열을 객체 키로 간주
        return s3Url.split(bucketName + "/")[1];
    }

    public void delete(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to delete file from S3", e);
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_FILE);
        }
    }
}
