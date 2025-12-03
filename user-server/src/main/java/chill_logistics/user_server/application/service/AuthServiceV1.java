package chill_logistics.user_server.application.service;

import chill_logistics.user_server.application.command.*;
import chill_logistics.user_server.domain.entity.RoleMapper;
import chill_logistics.user_server.domain.entity.User;
import chill_logistics.user_server.domain.entity.UserRole;
import chill_logistics.user_server.domain.repository.UserRepository;
import lib.entity.Role;
import lib.jwt.JwtTokenProvider;
import lib.jwt.TokenBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceV1 {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    public LoginResultV1 login(LoginCommandV1 command) {

        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Role libRole = RoleMapper.toLibRole(user.getRole());

        String accessToken = jwtTokenProvider.issueAccessToken(
                user.getId(),
                libRole,
                user.getUsername()
        );

        String refreshToken = jwtTokenProvider.issueRefreshToken(
                user.getId(),
                libRole,
                user.getUsername()
        );

        return new LoginResultV1(accessToken, refreshToken);
    }

    // 회원가입
    @Transactional
    public void signup(SignupCommandV1 command) {

        if (userRepository.existsByEmail(command.email())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        String encodedPw = passwordEncoder.encode(command.password());

        User user = User.createForSignup(
                command.email(),
                command.username(),
                command.nickname(),
                encodedPw,
                UserRole.MASTER
        );

        // AuditorAware + JPA Auditing이 createdBy / updatedBy 를 자동으로 채워줌 (SYSTEM UUID)
        userRepository.save(user);
    }

    // 리프레시 토큰 발급
    public ReissueTokenResultV1 reissueToken(ReissueTokenCommandV1 command) {

        String refreshToken = command.refreshToken();

        if(!jwtTokenProvider.validate(refreshToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        TokenBody tokenBody = jwtTokenProvider.parseJwt(refreshToken);

        if(!"refresh".equals(tokenBody.getType())) {
            throw new IllegalArgumentException("refresh 토큰이 아닙니다.");
        }

        User user = userRepository.findById(tokenBody.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Role libRole = RoleMapper.toLibRole(user.getRole());

        String newAccessToken = jwtTokenProvider.issueAccessToken(
                user.getId(),
                libRole,
                user.getUsername()
        );

        return new ReissueTokenResultV1(newAccessToken, refreshToken);
    }

}
