package com.entseeker.Hotel.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    @Value("${firebase.storage.bucket.url}")
    private String firebaseStorageBucket;

    public String saveImageToFirebaseStorage(MultipartFile photo) {
        try {
            String firebaseFilename = photo.getOriginalFilename();
            if (firebaseFilename == null) {
                throw new IllegalArgumentException("Invalid file name.");
            }

            // Use Google Application Default Credentials
            Storage storage = StorageOptions.getDefaultInstance().getService();
            InputStream inputStream = photo.getInputStream();

            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(firebaseStorageBucket, firebaseFilename))
                    .setContentType(photo.getContentType())
                    .build();

            storage.create(blobInfo, inputStream.readAllBytes());

            String downloadUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    firebaseStorageBucket, firebaseFilename);
            logger.info("File uploaded to: {}", downloadUrl);

            return downloadUrl;
        } catch (Exception e) {
            logger.error("Unable to upload image to Firebase Storage", e);
            throw new RuntimeException("Unable to upload image to Firebase Storage: " + e.getMessage());
        }
    }
}
