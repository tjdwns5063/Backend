package Lingtning.new_match42.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MatchRoomResponse {
    private Long id;
    private Integer size;
    private Integer capacity;
    private String matchType;
    private String matchStatus;
}
