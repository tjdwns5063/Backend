package Lingtning.new_match42.controller;

import Lingtning.new_match42.config.OAuth.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "LoginController")
@RestController
@RequestMapping("/login")
@Tag(name = "OAuth", description = "42 OAuth 인증 API")
public class LoginController {
    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.port}")
    private String serverPort;
//    private final OAuth2Service oAuth2Service;

//    @Autowired
//    public LoginController(OAuth2Service oAuth2Service) {
//        this.oAuth2Service = oAuth2Service;
//    }

//    @GetMapping("/oauth2/code")
//    @Operation(summary = "Redirect URL", description = "42 로그인 후 리다이렉트 처리 API", responses = {
//            @ApiResponse(responseCode = "301", description = "로그인 처리 완료")
//    })
//    public ResponseEntity<?> redirectURL(@RequestParam("code") String code) {
//        // 42 로그인 후 토큰 받아오기
////        String token = oAuth2Service.getToken(code);
//        String token = code;
//        log.info("token: " + token);
//        // todo: 토큰으로 유저 정보 받아오기
//        // todo: 유저 정보로 DB에 저장하기
//        // todo: 유저 정보로 JWT 토큰 생성하기
//        // todo: JWT 토큰 전달
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "http://" + serverIp + ":" + serverPort + "/api/v1/OAuth/login?token=" + token);
//        return new ResponseEntity<>(code, headers, HttpStatus.MOVED_PERMANENTLY);
//    }
}
