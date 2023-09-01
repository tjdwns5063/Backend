package Lingtning.new_match42.controller;

import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.service.FCMService;
import Lingtning.new_match42.service.FirebaseService;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** test **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/firebase")
@Tag(name = "firebasetest", description = "테스트 API")
public class FirebaseController {
    private final FirebaseService firebaseService;
    private final FCMService fcmService;
    private final UserService userService;

    @GetMapping("/test")
    @Operation(summary = "Hello!", description = "처음으로 만든 API", responses = {
            @ApiResponse(responseCode = "200", description = "야호! 성공!!!")
    })
    public List<Map<String, Object>> helloFirebase() {
        // FirebaseService를 통한 작업 수행
        List<Map<String, Object>> firebaseDataList = firebaseService.readAllDataFromRoomsCollection();
        return firebaseDataList; // 모든 Firebase 데이터를 반환
    }

    @PostMapping("/token/subscribe")
    @Operation(summary = "FCM 토큰 등록 API", description = "FCM 토큰을 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 토큰 등록 완료")
    })
    public void subscribeToken(Authentication authentication, @RequestParam String token) {
        User user = userService.getUser(authentication);
        fcmService.subscribeToken(user, token);
    }

    @PostMapping("/message/send/{userId}")
    @Operation(summary = "FCM 메시지 전송 API", description = "FCM 메시지를 전송하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 메시지 전송 완료")
    })
    public void sendChatMessage(@PathVariable Long userId, @RequestParam String message) {
        User user = userService.getUser(userId);
        fcmService.sendChatMessage(user, message);
    }
}
