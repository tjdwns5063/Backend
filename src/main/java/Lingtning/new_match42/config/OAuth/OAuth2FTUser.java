package Lingtning.new_match42.config.OAuth;

import Lingtning.new_match42.entity.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OAuth2FTUser implements OAuth2User {
//    String getIntra();
//    String getEmail();
//    String getOauth2Id();

    private final User user;
    private final Map<String, Object> attributes;

    @Builder
    public OAuth2FTUser(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().toString());
        return collect;
    }

    @Override
    public String getName() {
        return attributes.get("login").toString();
    }
}
