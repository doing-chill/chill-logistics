package lib.security;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lib.entity.Role;
import lib.jwt.JwtTokenProvider;
import lib.jwt.TokenBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> permitAllPrefixes;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   Set<String> permitAllPrefixes) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.permitAllPrefixes = permitAllPrefixes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 화이트리스트: prefix 기준으로 통과
        if (permitAllPrefixes != null &&
                permitAllPrefixes.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) {
            token = token.substring(6).trim();
        }

        if (!jwtTokenProvider.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);

        // access 토큰만 허용
        if (!"access".equals(tokenBody.getType())) {
            filterChain.doFilter(request, response);
            return;
        }

        Role role = tokenBody.getRole();
        var authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        tokenBody,   // principal = TokenBody
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
