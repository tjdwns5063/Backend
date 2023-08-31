package Lingtning.new_match42.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchStatus {

    WAITING("WAITING"), MATCHED("MATCHED");

    private final String key;
}
