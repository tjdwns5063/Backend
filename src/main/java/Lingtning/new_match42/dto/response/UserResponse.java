package Lingtning.new_match42.dto.response;

import Lingtning.new_match42.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserResponse {
    private Long id;
    private String intra;
    private String email;
    private Role role;

    private List<String> interests;
    private Long interestCount;

    private List<String> blockUsers;
    private Long blockCount;
}
