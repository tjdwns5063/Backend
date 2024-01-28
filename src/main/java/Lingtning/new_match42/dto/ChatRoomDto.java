package Lingtning.new_match42.dto;

import java.util.List;

public record ChatRoomDto(String id, String name, String msg, List<Long> userIds) {
}
