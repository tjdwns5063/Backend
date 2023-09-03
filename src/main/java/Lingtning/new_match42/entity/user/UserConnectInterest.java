package Lingtning.new_match42.entity.user;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 유저와 관심사를 연결하는 테이블
@Entity
@Table(name = "user_connect_interest")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class UserConnectInterest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;

    @Builder
    public UserConnectInterest(Long id, User user, Interest interest) {
        this.id = id;
        this.user = user;
        this.interest = interest;
    }
}
