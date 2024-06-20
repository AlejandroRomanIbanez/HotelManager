package com.entseeker.Hotel.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    private String firebaseStorageBucket;

    public String saveImageToFirebaseStorage(MultipartFile photo) {
        String firebaseStorageUrl = null;

        try {
            String firebaseFilename = photo.getOriginalFilename();

            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            InputStream inputStream = photo.getInputStream();

            assert firebaseFilename != null;
            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(firebaseStorageBucket, firebaseFilename))
                    .setContentType("image/jpeg")
                    .build();
            storage.create(blobInfo, inputStream.readAllBytes());

            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    firebaseStorageBucket, firebaseFilename);
        } catch (Exception e) {
            logger.error("Unable to upload image to Firebase Storage: {}", e.getMessage());
            throw new RuntimeException("Unable to upload image to Firebase Storage: " + e.getMessage());
        }
    }
}
