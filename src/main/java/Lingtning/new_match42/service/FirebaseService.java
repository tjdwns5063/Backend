package Lingtning.new_match42.service;

import Lingtning.new_match42.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseService {

    public Map<String, Object> readAndWriteData() {
        final Firestore client = FirestoreClient.getFirestore();
        Map<String, Object> data = new HashMap<>();

        ApiFuture<DocumentSnapshot> result = client.collection("rooms").document("1").get();

        try {
            DocumentSnapshot document = result.get();
            if (document.exists()) {
                data = document.getData();
                System.out.println("Document data: " + data);
            } else {
                System.out.println("No such document");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return data; // Firestore 데이터를 반환
    }
}