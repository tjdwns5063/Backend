package Lingtning.new_match42.dto;

import Lingtning.new_match42.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String intra;
    private String email;
    private Role role;

    private List<String> interests;
    private Integer interestCount;

    private List<String> blockUsers;
    private Integer blockCount;
}
