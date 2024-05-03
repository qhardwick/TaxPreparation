package com.skillstorm.services;

import java.io.InputStream;

public interface S3Service {

    // Upload file to S3 Bucket:
    public void uploadFile(String key, byte[] file);

    // Download file from S3 Bucket:
    public InputStream getObject(String key);
}
