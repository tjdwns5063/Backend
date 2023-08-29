//package Lingtning.new_match42.service;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.database.*;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class FirebaseConnectionTest {
//
//    public static void main(String[] args) throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("/Users/ilko/goinfre/Backend/src/main/resources/firebase-adminsdk.json");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                //.setDatabaseUrl("https://your-project-id.firebaseio.com") // Firebase 프로젝트 URL
//                .build();
//
//        FirebaseApp.initializeApp(options);
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference usersReference = databaseReference.child("users");
//
//        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("Data in 'users' node:");
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    User user = userSnapshot.getValue(User.class);
//                    System.out.println("Name: " + user.getName() + ", Email: " + user.getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("Error reading data: " + databaseError.getMessage());
//            }
//        });
//
//        // 데이터 쓰기
//        User newUser = new User("John Doe", "john@example.com");
//        Map<String, Object> userValues = newUser.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        //childUpdates.put("/users/user3", userValues);
//        //databaseReference.updateChildren(childUpdates);
//    }
//
//    static class User {
//        private String name;
//        private String email;
//
//        public User() {
//        }
//
//        public User(String name, String email) {
//            this.name = name;
//            this.email = email;
//        }
//
//        // Getter, Setter methods
//
//        public Map<String, Object> toMap() {
//            HashMap<String, Object> result = new HashMap<>();
//            result.put("name", name);
//            result.put("email", email);
//            return result;
//        }
//
//        public String getName() {//test
//            return null;
//        }
//
//        public String getEmail() {//test
//            return null;
//        }
//    }
//}
