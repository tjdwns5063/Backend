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
public class UserInterestResponse {
    private Long id;
    private String intra;

    private List<String> interests;
}
