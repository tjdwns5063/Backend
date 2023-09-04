package Lingtning.new_match42.dto;

import Lingtning.new_match42.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsocketDto {
    private MessageType type;
    private String content;
    private String sender;
}