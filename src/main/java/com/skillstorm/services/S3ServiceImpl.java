package com.skillstorm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3;
    private final String bucketName;

    @Autowired
    public S3ServiceImpl(S3Client s3, @Value("${BUCKET_NAME}") String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    // Upload file to S3 Bucket:
    public void uploadFile(String key, byte[] file) {
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(), RequestBody.fromBytes(file));
    }

    // Download file from S3 Bucket:
    public InputStream getObject(String key) {
        return s3.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build());
    }
}
