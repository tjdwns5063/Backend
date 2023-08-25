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

    @GetMapping("/")
    @Operation(summary = "Login API", description = "42 로그인 후 JWT 반환", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 처리 완료")
    })
    public ResponseEntity<?> loginJWT() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "token=1234");
        return new ResponseEntity<>("로그인 성공", headers, HttpStatus.OK);
    }
}
