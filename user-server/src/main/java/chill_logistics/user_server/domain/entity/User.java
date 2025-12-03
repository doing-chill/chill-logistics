package chill_logistics.user_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "username", nullable = false, length = 15)
    private String username;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "signup_status", nullable = false, length = 20)
    private SignupStatus signupStatus;

    protected User() {}

    public static User createForSignup(
            String email,
            String username,
            String nickname,
            String encodedPassword,
            UserRole role
    ) {
        User user = new User();
        user.email = email;
        user.username = username;
        user.nickname = nickname;
        user.password = encodedPassword;
        user.role = role;
        user.signupStatus = SignupStatus.PENDING; // 기본 가입 상태
        return user;
    }
}
