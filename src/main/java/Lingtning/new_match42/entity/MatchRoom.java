package Lingtning.new_match42.entity;

import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

// 매칭 방 테이블
@Entity
@Table(name = "match_room")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "matchList")
public class MatchRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MatchType matchType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MatchStatus matchStatus;

    @OneToMany(mappedBy = "matchRoom")
    private List<MatchList> matchList;

    @Builder
    public MatchRoom(Integer size, Integer capacity, MatchType matchType, MatchStatus matchStatus) {
        this.size = size;
        this.capacity = capacity;
        this.matchType = matchType;
        this.matchStatus = matchStatus;
    }
}
