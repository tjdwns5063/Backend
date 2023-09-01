package Lingtning.new_match42.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatRequest {
    private Integer capacity;

    @Builder
    public ChatRequest(Integer capacity) {
        this.capacity = capacity;
    }
}
