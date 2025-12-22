package lib.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lib.entity.Role;
import lib.passport.PassportHeaders;
import lib.passport.PassportValidator;
import lib.passport.ServicePassport;
import lib.passport.ServicePrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class PassportAuthenticationFilter extends OncePerRequestFilter {

    private final PassportValidator passportValidator;
    private final Set<String> permitAllPrefixes;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 화이트리스트
        if (permitAllPrefixes != null && permitAllPrefixes.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1) Passport headers 읽기
        String issuer = request.getHeader(PassportHeaders.PASSPORT_ISSUER);
        String service = request.getHeader(PassportHeaders.PASSPORT_SERVICE);
        String iatStr = request.getHeader(PassportHeaders.PASSPORT_IAT);
        String sig = request.getHeader(PassportHeaders.PASSPORT_SIG);

        if (issuer == null || service == null || iatStr == null || sig == null) {
            // 내부 호출인데 passport가 없으면 인증 실패
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        long iat;
        try {
            iat = Long.parseLong(iatStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ServicePassport passport = new ServicePassport(issuer, service, iat, sig);

        if (!passportValidator.validate(passport)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 2) User Context 읽기 (없을 수도 있음 = SYSTEM)
        String userIdStr = request.getHeader(PassportHeaders.USER_ID);
        String userRoleStr = request.getHeader(PassportHeaders.USER_ROLE);

        LoginUser loginUser = null;
        List<SimpleGrantedAuthority> authorities = List.of();

        if (userIdStr != null && userRoleStr != null) {
            try {
                UUID userId = UUID.fromString(userIdStr);
                String roleName = userRoleStr.trim();

                // Role enum 매핑
                Role role = Role.valueOf(roleName);

                loginUser = new LoginUser(userId, null, role.name());

                authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
            } catch (Exception e) {
                // user header가 이상하면 SYSTEM으로 보지 말고 실패 처리하는 게 안전
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        ServicePrincipal principal = new ServicePrincipal(service, loginUser);

        // 3) SecurityContext 세팅
        var authentication = new PassportAuthentication(principal, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
