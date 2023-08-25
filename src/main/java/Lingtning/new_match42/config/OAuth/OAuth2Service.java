package Lingtning.new_match42.config.OAuth;

import Lingtning.new_match42.entity.Role;
import Lingtning.new_match42.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        OAuth2User user = super.loadUser(userRequest);

        log.info("OAuth Login: {}", userRequest.getClientRegistration().getRegistrationId());

        return OAuth2FTUser.builder()
                .user(User.builder()
                        .intra(user.getAttribute("login"))
                        .email(user.getAttribute("email"))
                        .role(Role.USER)
                        .build())
                .attributes(user.getAttributes())
                .build();
    }
}
