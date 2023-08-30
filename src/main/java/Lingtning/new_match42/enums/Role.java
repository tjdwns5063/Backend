package Lingtning.new_match42.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 로그인 사용자 권한
@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String key;

}