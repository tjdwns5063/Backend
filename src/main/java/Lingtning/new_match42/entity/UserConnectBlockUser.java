package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user_connect_block_user")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class UserConnectBlockUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_user_id")
    private User blockUser;

    @Builder
    public UserConnectBlockUser(Long id, User user, User blockUser) {
        this.id = id;
        this.user = user;
        this.blockUser = blockUser;
    }
}
