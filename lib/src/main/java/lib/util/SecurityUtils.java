package lib.util;

import lib.entity.Role;
import lib.jwt.TokenBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<TokenBody> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof TokenBody tokenBody){
            return Optional.of(tokenBody);
        }

        return Optional.empty();
    }

    // UserId 가져오는 메서드 (인증 필수)
    public static UUID getCurrentUserId() {
        return getCurrentUser()
                .map(TokenBody::getUserId)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자(userId)가 없습니다."));
    }

    // UserName 가져오는 메서드 (인증 필수)
    public static String getCurrentUserName() {
        return getCurrentUser()
                .map(TokenBody::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자(userName)가 없습니다."));
    }

    // 인증된 사용자 역할을 가져오는 메서드 (인증 필수)
    public static Role getCurrentRole() {
        return getCurrentUser()
                .map(TokenBody::getRole)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자(userRole)가 없습니다."));
    }

    public static Optional<Role> getCurrentRoleOptional() {
        return getCurrentUser().map(TokenBody::getRole);
    }

    // 유저 권한 체크 메서드(단일 버전)
    public static boolean hasRole(Role role){
        return getCurrentRoleOptional()
                .map(r -> r == role)
                .orElse(false);
    }

    // 유저 권한 체크 메서드(복수 버전)
    public static boolean hasAnyRole(Role... roles){
        return getCurrentRoleOptional()
                .map(r -> Arrays.asList(roles).contains(r))
                .orElse(false);
    }
}
