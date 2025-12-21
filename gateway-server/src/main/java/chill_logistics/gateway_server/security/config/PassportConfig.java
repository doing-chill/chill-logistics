package chill_logistics.gateway_server.security.config;

import lib.passport.PassportIssuer;
import lib.passport.PassportProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PassportConfig {

    @Bean
    public PassportProperties passportProperties(
            @Value("${custom.passport.issuer:gateway}") String issuer,
            @Value("${custom.passport.secret}") String secret,
            @Value("${custom.passport.allowed-skew-ms:300000}") long allowedSkewMs
    ) {
        return new PassportProperties(issuer, secret, allowedSkewMs);
    }

    @Bean
    public PassportIssuer passportIssuer(PassportProperties props) {
        return new PassportIssuer(props);
    }
}
