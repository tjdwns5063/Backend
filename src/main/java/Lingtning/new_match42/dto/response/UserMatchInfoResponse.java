package Lingtning.new_match42.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserMatchInfoResponse {
    private Long id;
    private Long mealMatchId;
    private Long subjectMatchId;
    private Long chatMatchId;
}
