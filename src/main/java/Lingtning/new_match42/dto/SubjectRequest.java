package Lingtning.new_match42.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
public class SubjectRequest extends MatchRequest{
    private String project;
}
