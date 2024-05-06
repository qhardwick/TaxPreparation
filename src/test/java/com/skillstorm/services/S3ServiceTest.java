package com.skillstorm.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks private static S3ServiceImpl s3Service;
    @Mock private static S3Client s3Client;

    private final String bucketName = "test-bucket";

    @BeforeEach
    public void setUp() {
        s3Service = new S3ServiceImpl(s3Client, bucketName);
    }

    @Test
    public void testUploadFile() {
        // Arrange
        String key = "test-key";
        byte[] fileContent = "Test File Content".getBytes();

        // Act
        s3Service.uploadFile(key, fileContent);

        // Assert
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    //@Test
    public void testGetObject() {
        // Arrange
        String key = "test-key";
        InputStream inputStream = new ByteArrayInputStream("Test File Content".getBytes());
        //GetObjectResponse getObjectResponse = GetObjectResponse.builder().responseInputStream(inputStream).build();
        //when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(getObjectResponse);

        // Act
        InputStream result = s3Service.getObject(key);

        // Assert
        assertNotNull(result);
        // You can add further assertions on the InputStream if required
    }
}
