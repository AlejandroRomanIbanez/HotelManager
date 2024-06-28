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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    @Value("${firebase.storage.bucket.url}")
    private String firebaseStorageBucket;

    @Value("${firebase.credentials.file.path}")
    private String credentialsFilePath;

    private Storage getStorage() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFilePath))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String saveImageToFirebaseStorage(MultipartFile photo) {
        try {
            String firebaseFilename = photo.getOriginalFilename();
            if (firebaseFilename == null) {
                throw new IllegalArgumentException("Invalid file name.");
            }

            Storage storage = getStorage();
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
