package Lingtning.new_match42.dto;

import java.util.List;

public record ChatCreateDto(Long capacity, String matchType, String createdAt, List<Long> ids) {

}
