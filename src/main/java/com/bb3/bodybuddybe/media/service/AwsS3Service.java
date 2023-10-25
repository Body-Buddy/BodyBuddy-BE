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
public class AwsS3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public String uploadFile(MultipartFile file) {
        validateFile(file);
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

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            log.error("File size exceeds the limit.");
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDS_LIMIT);
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
            log.error("Invalid file type.");
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String generateFileName(String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    public void deleteFileFromS3Url(String s3Url) {
        String fileName = extractFileNameFromS3Url(s3Url);
        deleteFile(fileName);
    }

    private String extractFileNameFromS3Url(String s3Url) {
        if (!s3Url.contains(bucketName)) {
            log.error("Invalid S3 URL: {}", s3Url);
            throw new CustomException(ErrorCode.INVALID_S3_URL);
        }
        return s3Url.split(bucketName + "/")[1];
    }

    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to delete file from S3", e);
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_FILE);
        }
    }
}
