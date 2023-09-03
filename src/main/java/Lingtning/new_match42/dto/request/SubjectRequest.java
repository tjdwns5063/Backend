package Lingtning.new_match42.dto.request;

import Lingtning.new_match42.dto.request.MatchRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class SubjectRequest extends MatchRequest {
    private String project;
}
