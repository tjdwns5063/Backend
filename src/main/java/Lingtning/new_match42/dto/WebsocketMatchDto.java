package Lingtning.new_match42.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsocketMatchDto {
    private Long id;
    private Integer size;
    private Integer capacity;
    private String matchType;
    private String matchStatus;
}