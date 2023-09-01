package Lingtning.new_match42.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseService {

    public List<Map<String, Object>> readAllDataFromRoomsCollection() {
        final Firestore client = FirestoreClient.getFirestore();
        List<Map<String, Object>> dataList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = client.collection("rooms").get();

        try {
            QuerySnapshot snapshot = querySnapshot.get();
            List<QueryDocumentSnapshot> documents = snapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> data = document.getData();
                log.info("Firebase Data: " + data);
               // System.out.println("문서 데이터: " + data);
                dataList.add(data);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return dataList; // firebase 데이터 전체 리턴
    }
}
