package lib.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PassportAuthentication extends UsernamePasswordAuthenticationToken {

    public PassportAuthentication(Object principal,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
    }
}
