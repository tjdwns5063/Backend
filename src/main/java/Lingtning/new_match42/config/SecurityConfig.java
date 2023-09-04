package Lingtning.new_match42.config;

import Lingtning.new_match42.config.OAuth.ConfigFailureHandler;
import Lingtning.new_match42.config.OAuth.ConfigSuccessHandler;
import Lingtning.new_match42.config.OAuth.OAuth2Service;
import Lingtning.new_match42.config.jwt.JwtAuthenticationFilter;
import Lingtning.new_match42.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// spring security 설정 파일 입니다.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final OAuth2Service oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final ConfigSuccessHandler configSuccessHandler;
    private final ConfigFailureHandler configFailureHandler;

    private final String[] WHITE_LIST = {
            // swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            // oauth2
            "/oauth2/**",
            "/login/oauth2/**",
            // login
            "/login/failure",
            "/api/v1/login/success",
            // logout
            "/api/v1/logout",
            // websocket
            "/ws/**",
            "/message/**",
            "/room_name/**",
            // error
            "/error"
    };
    private final String[] USER_ACCESS_LIST = {
            "/api/v1/**"
    };
    private final String[] ADMIN_ACCESS_LIST = {
            "/api/v1/admin/**"
    };

    @Autowired
    public SecurityConfig(OAuth2Service oAuth2Service, JwtTokenProvider jwtTokenProvider, ConfigSuccessHandler configSuccessHandler, ConfigFailureHandler configFailureHandler) {
        this.oAuth2Service = oAuth2Service;
        this.jwtTokenProvider = jwtTokenProvider;
        this.configSuccessHandler = configSuccessHandler;
        this.configFailureHandler = configFailureHandler;
    }

    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()  // 모든 사용자 허용 경로
                        .requestMatchers(USER_ACCESS_LIST).hasRole("USER")  // USER 권한 필요 경로
                        .requestMatchers(ADMIN_ACCESS_LIST).hasRole("ADMIN")  // ADMIN 권한 필요 경로
                        .anyRequest().authenticated())  // 그 외 나머지 경로는 전부 인증 필요
                // 예외 처리
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                        })) // 권한 없음
                // 로그인
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service)) // 유저 정보 가져오기
                        .successHandler(configSuccessHandler) // 로그인 성공
                        .failureHandler(configFailureHandler) // 로그인 실패
                        .permitAll()
                )
                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
