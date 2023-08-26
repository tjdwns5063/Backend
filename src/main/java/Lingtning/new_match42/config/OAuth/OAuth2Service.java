package Lingtning.new_match42.config.OAuth;

import Lingtning.new_match42.entity.Role;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

@Slf4j
@Service
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Autowired
    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String intra = oAuth2User.getAttribute("login");
        log.info("OAuth Login: {}", intra);

        User user = userRepository.findByIntra(oAuth2User.getAttribute("login")).orElse(null);
        if (user == null) {
            userRepository.save(User.builder()
            .intra(oAuth2User.getAttribute("login"))
            .email(oAuth2User.getAttribute("email"))
            .role(Role.USER)
            .build());
        }


        return OAuth2FTUser.builder()
                .user(user)
                .attributes(oAuth2User.getAttributes())
                .build();
    }
}
