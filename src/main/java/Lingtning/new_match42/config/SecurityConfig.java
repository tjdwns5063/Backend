package Lingtning.new_match42.config;

import Lingtning.new_match42.config.OAuth.ConfigFailureHandler;
import Lingtning.new_match42.config.OAuth.ConfigSuccessHandler;
import Lingtning.new_match42.config.OAuth.OAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/login/oauth2/code/**",
            "/login/success",
            "/api/v1/**"
    };

    private final String[] USER_ACCESS_LIST = {
            "/api/v1/user/**"
    };

    private final String[] ADMIN_ACCESS_LIST = {
            "/api/v1/admin/**"
    };

    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(USER_ACCESS_LIST).hasRole("USER")
                        .requestMatchers(ADMIN_ACCESS_LIST).hasRole("ADMIN")
                        .anyRequest().authenticated())
//                // 예외 처리
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                        })) // 권한 없음
                // 로그인
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(new OAuth2Service())) // 유저 정보 가져오기
                        .successHandler(new ConfigSuccessHandler()) // 로그인 성공 시 처리
                        .failureHandler(new ConfigFailureHandler()) // 로그인 실패 시 처리
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/api/v1/user/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화
        return http.build();
    }
}