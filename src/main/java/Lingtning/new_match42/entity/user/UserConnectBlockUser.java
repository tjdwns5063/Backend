package Lingtning.new_match42.entity.user;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 유저와 차단유저를 연결하는 테이블
@Entity
@Table(name = "user_connect_block_user")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class UserConnectBlockUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_user_id", nullable = false)
    private User blockUser;

    @Builder
    public UserConnectBlockUser(Long id, User user, User blockUser) {
        this.id = id;
        this.user = user;
        this.blockUser = blockUser;
    }
}
