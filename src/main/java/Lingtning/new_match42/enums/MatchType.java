package Lingtning.new_match42.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 매칭의 종류
@Getter
@RequiredArgsConstructor
public enum MatchType {

    CHAT("CHAT"), SUBJECT("SUBJECT"), MEAL("MEAL");

    private final String key;
}
