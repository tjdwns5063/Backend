package Lingtning.new_match42.service;

import Lingtning.new_match42.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseService {

    public void readAndWriteData() {
        final Firestore client = FirestoreClient.getFirestore();

        ApiFuture<DocumentSnapshot> result = client.collection("rooms").document("1").get();

        try {
            DocumentSnapshot document = result.get(); // 비동기 작업이 완료될 때까지 대기하고 결과 가져오기
            if (document.exists()) {
                System.out.println("Document data: aaaaaaa" + document.getData());
            } else {
                System.out.println("No such documentaaaaaaaaa");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference usersReference = databaseReference.child("users");
//
//        // 데이터 읽기
//        usersReference.child("user1").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                log.info("테스트");
//                log.info("테스트");
//                log.info("테스트");
//                if (dataSnapshot.exists()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    System.out.println(user.getName());
//                } else {
//                    System.out.println("User not found");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("Error reading user data");
//            }
//        });
//
//        // 데이터 쓰기
//        User newUser = new User();
//        usersReference.child("user2");
        //usersReference.setValue(newUser);
    }
}
