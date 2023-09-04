package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.WebsocketMatchDto;
import Lingtning.new_match42.dto.WebsocketDto;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j(topic = "WEBSOCKET")
@RequiredArgsConstructor
public class WebsocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final static List<String> users = new ArrayList<>();

    // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
    // @EventListener은 한개의 매개변수만 가질 수 있다.
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("Received a new web socket connection with event : " + event);
    }

    // 사용자가 웹 소켓 연결을 끊으면 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() == null) {
            return;
        }
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        log.info("username : " + username);
        if(username != null) {
            log.info("User Disconnected : " + username);

            users.remove(username);

            WebsocketDto message = new WebsocketDto(MessageType.LEAVE, null, username);
            messagingTemplate.convertAndSend("/room_name/public", message);
        }
    }

    // /message/sendMessage로 요청이 들어오면 해당 메소드로 처리된다.
    @MessageMapping("/sendMessage")
    @SendTo("/room_name/public")
    public WebsocketDto sendMessage(@Payload WebsocketDto message) {
        log.info("Message : " + message);
        return message;
    }

    // /message/addUser로 요청이 들어오면 해당 메소드로 처리된다.
    @MessageMapping("/addUser")
    @SendTo("/room_name/public")
    public WebsocketDto addUser(@Payload WebsocketDto message, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() == null) {
            log.info("SessionAttributes is null");
            return WebsocketDto.builder()
                    .type(MessageType.JOIN)
                    .content("fail")
                    .sender(message.getSender())
                    .build();
        }
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        users.add(message.getSender());
        log.info("User Added : " + message.getSender());
        return message;
    }

    // /message/getRoomInfo로 요청이 들어오면 해당 메소드로 처리된다.
    @MessageMapping("/getRoomInfo")
    public void getRoomInfo(@Payload WebsocketMatchDto message) {
        // 매칭이 완료되면
        log.info("Message : " + message);
        if (message.getSize() >= message.getCapacity()) {
            log.info("match complete");
            message.setMatchStatus(MatchStatus.MATCHED.getKey());
        }
        messagingTemplate.convertAndSend("/room_name/public/" + message.getId().toString(), message);
    }
}
