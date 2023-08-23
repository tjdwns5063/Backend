package Lingtning.new_match42.controller;

import Lingtning.new_match42.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/OAuth")
@Tag(name = "OAuth", description = "42 OAuth 인증 API")
public class OAuthController {
    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.port}")
    private String serverPort;
    private final OAuthService oAuthService;

    @Autowired
    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/redirect")
    @Operation(summary = "Redirect URL", description = "42 로그인 후 리다이렉트 처리 API", responses = {
            @ApiResponse(responseCode = "301", description = "로그인 처리 완료")
    })
    public ResponseEntity<?> redirectURL(@RequestParam("code") String code) {
        // 42 로그인 후 토큰 받아오기
        String token = oAuthService.getToken(code);
        // todo: 토큰으로 유저 정보 받아오기
        // todo: 유저 정보로 DB에 저장하기
        // todo: 유저 정보로 JWT 토큰 생성하기
        // todo: JWT 토큰 전달
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://" + serverIp + ":" + serverPort + "/api/v1/OAuth/login?token=" + token);
        return new ResponseEntity<>(code, headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
