package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 유저와 매칭 방을 연결하는 매칭 테이블
@Entity
@Table(name = "match_list")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class MatchList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "match_room_id", nullable = false)
    private MatchRoom matchRoom;
}
