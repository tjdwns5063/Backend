package Lingtning.new_match42.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class MatchRequest {
    private Integer capacity;
}
