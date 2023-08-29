package Lingtning.new_match42.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserInterestResponse {
    private Long id;
    private String intra;

    private List<String> interests;
}
