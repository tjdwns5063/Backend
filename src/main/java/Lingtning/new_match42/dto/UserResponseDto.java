package Lingtning.new_match42.dto;

import Lingtning.new_match42.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String intra;
    private String email;
    private Role role;

    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private String interest5;
    private Integer interestCount;

    private String blockUser1;
    private String blockUser2;
    private String blockUser3;
    private String blockUser4;
    private String blockUser5;
    private Integer blockCount;
}
