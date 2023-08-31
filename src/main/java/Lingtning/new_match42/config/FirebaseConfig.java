package Lingtning.new_match42.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public void initializeFirebase() throws IOException {
        InputStream serviceAccount = FirebaseConfig.class.getResourceAsStream("/firebase-adminsdk.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                //.setDatabaseUrl("https://your-project-id.firebaseio.com") // Firebase 프로젝트 URL
                .build();

        FirebaseApp.initializeApp(options);
    }
}
