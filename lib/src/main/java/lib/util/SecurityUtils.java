package lib.util;

import lib.entity.Role;
import lib.jwt.TokenBody;
import lib.passport.ServicePrincipal;
import lib.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<LoginUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return Optional.empty();

        Object principal = authentication.getPrincipal();

        if (principal instanceof LoginUser loginUser) {
            return Optional.of(loginUser);
        }

        if (principal instanceof ServicePrincipal sp && sp.loginUser() != null) {
            return Optional.of(sp.loginUser());
        }

        return Optional.empty();
    }

    // UserId 가져오는 메서드 (인증 필수)
    public static UUID getCurrentUserId() {
        return getCurrentUser()
                .map(LoginUser::id)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자(userId)가 없습니다."));
    }

    // UserEmail 가져오는 메서드 (인증 필수)
    public static String getCurrentUserName() {
        return getCurrentUser()
                .map(LoginUser::email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자(userEmail)가 없습니다."));
    }

    // 인증된 사용자 역할을 가져오는 메서드 (인증 필수)
    public static Role getCurrentRole() {
        return getCurrentUser()
                .map(u -> Role.valueOf(u.role()))
                .orElseThrow(() -> new IllegalStateException("인증된 사용자(role)가 없습니다."));
    }

    // 유저 권한 체크 메서드(단일 버전)
    public static boolean hasRole(Role role) {
        return getCurrentUser()
                .map(u -> Role.valueOf(u.role()) == role)
                .orElse(false);
    }
}
