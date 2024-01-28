package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.ChatRoomDto;
import Lingtning.new_match42.dto.ChatCreateDto;
import Lingtning.new_match42.dto.MatchDto;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.service.FirebaseService;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * test
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/firebase")
@Tag(name = "firebasetest", description = "테스트 API")
public class FirebaseController {
    private final FirebaseService firebaseService;
    private final UserService userService;


    @PostMapping("/token/subscribe")
    @Operation(summary = "FCM 토큰 등록 API", description = "FCM 토큰을 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 토큰 등록 완료")
    })
    public ResponseEntity<?> subscribeToken(Authentication authentication, @RequestParam String token) {
        User user = userService.getUser(authentication);
        return firebaseService.subscribeToken(user, token);
    }

    @PostMapping("/message/send/chat")
    @Operation(summary = "채팅 메세지 전송 알림", description = "채팅 메세지 전송을 알리는 알림을 보내는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 메시지 전송 완료")
    })
    public ResponseEntity<?> sendChatMessage(@RequestBody
            ChatRoomDto dto, Authentication authentication) {

        User user = userService.getUser(authentication);
        return firebaseService.sendChatMessage(dto, user);
    }

    @PostMapping("/message/send/create_chat")
    @Operation(summary = "채팅방 생성 알림", description = "채팅방 생성을 알리는 알림을 보내는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 메시지 전송 완료")
    })
    public ResponseEntity<?> sendChatCreateMessage(@RequestBody ChatCreateDto dto) {
        return firebaseService.sendChatCreateMessage(dto);
    }

    @PostMapping("/message/send/match")
    @Operation(summary = "모두가 동의해서 매치가 됐음을 알림", description = "모두가 동의해서 매치가 됐음을 알리는 API", responses = {
            @ApiResponse(responseCode = "200", description = "FCM 메시지 전송 완료")
    })
    public ResponseEntity<?> sendMatchMessage(@RequestBody MatchDto dto) {
        return firebaseService.sendMatchMessage(dto);
    }
}
