package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.response.WebsocketDto;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.enums.MessageType;
import Lingtning.new_match42.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebsocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final static List<String> users = new ArrayList<>();

    // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
    // @EventListener은 한개의 매개변수만 가질 수 있다.
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        User user = (User) event.getUser();
        if (user == null) {
            log.info("Received a new web socket connection: NULL");
        } else {
            log.info("Received a new web socket connection: " + user.getUsername());
        }
    }

    // 사용자가 웹 소켓 연결을 끊으면 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() == null) {
            return;
        }
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            log.info("User Disconnected : " + username);

            users.remove(username);

            WebsocketDto chat = new WebsocketDto(MessageType.LEAVE, null, username);
            messagingTemplate.convertAndSend("/room_name/public", chat);
        }
    }

    // /message/sendMessage로 요청이 들어오면 해당 메소드로 처리된다.
    @MessageMapping("/sendMessage")
    @SendTo("/room_name/public")
    public WebsocketDto sendMessage(@Payload WebsocketDto message) {
        return message;
    }

    // /message/addUser로 요청이 들어오면 해당 메소드로 처리된다.
    @MessageMapping("/addUser")
    @SendTo("/room_name/public")
    public WebsocketDto addUser(@Payload WebsocketDto chat, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() == null) {
            return WebsocketDto.builder()
                    .type(MessageType.JOIN)
                    .content("fail")
                    .sender(chat.getSender())
                    .build();
        }
        headerAccessor.getSessionAttributes().put("username", chat.getSender());
        users.add(chat.getSender());
        return chat;
    }
}
