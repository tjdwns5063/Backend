package Lingtning.new_match42.entity;

import com.fasterxml.jackson.databind.util.ClassUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "intra", "email", "role"})
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String intra;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String email, String intra, Role role) {
        this.id = id;
        this.email = email;
        this.intra = intra;
        this.role = role;
    }

}