package Lingtning.new_match42.entity.match;

import Lingtning.new_match42.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 유저와 매칭 방을 연결하는 매칭 테이블
@Entity
@Table(name = "match_list")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
@EntityListeners(AuditingEntityListener.class)
public class MatchList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_room_id", nullable = false)
    private MatchRoom matchRoom;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public MatchList(User user, MatchRoom matchRoom) {
        this.user = user;
        this.matchRoom = matchRoom;
    }
}
