package Lingtning.new_match42.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserIntraRequest {
    private Long[] userIds;

    @Builder
    public UserIntraRequest(Long[] userIds) {
        this.userIds = userIds;
    }
}
