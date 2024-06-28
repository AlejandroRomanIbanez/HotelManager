package com.entseeker.Hotel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.sql.DriverManager.println;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("secret.properties")) {
            prop.load(input);
        }
        String filePath = "src/main/resources/firebase_credentials.json";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            filePath = filePath.replace("/", "\\");
        }
        FileInputStream serviceAccount = new FileInputStream(filePath);

        System.out.println(prop.getProperty("FIREBASE_STORAGE_BUCKET_URL"));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(prop.getProperty("FIREBASE_STORAGE_BUCKET_URL"))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
