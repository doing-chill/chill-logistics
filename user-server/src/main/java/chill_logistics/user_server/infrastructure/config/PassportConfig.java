package chill_logistics.user_server.infrastructure.config;

import lib.passport.PassportIssuer;
import lib.passport.PassportProperties;
import lib.passport.PassportValidator;
import lib.security.PassportAuthenticationFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class PassportConfig {

    /**
     * application.yml의 custom.passport.* 를 바인딩 받는 전용 DTO
     */
    @Bean
    @ConfigurationProperties(prefix = "custom.passport")
    public PassportProps passportProps() {
        return new PassportProps();
    }

    @Bean
    public PassportProperties passportProperties(PassportProps p) {
        // issuer 기본값 방어
        String issuer = (p.getIssuer() == null || p.getIssuer().isBlank()) ? "gateway" : p.getIssuer();
        return new PassportProperties(issuer, p.getSecret(), p.getAllowedSkewMs());
    }

    @Bean
    public PassportValidator passportValidator(PassportProperties props) {
        return new PassportValidator(props);
    }

    @Bean
    public PassportAuthenticationFilter passportAuthenticationFilter(PassportValidator validator) {
        return new PassportAuthenticationFilter(
                validator,
                Set.of(
                        "/favicon.ico",
                        "/error",
                        "/actuator",
                        "/actuator/"
                )
        );
    }

    @Bean
    public PassportIssuer passportIssuer(PassportProperties props) {
        return new PassportIssuer(props);
    }

    public static class PassportProps {
        private String issuer;
        private String secret;
        private long allowedSkewMs = 300_000;

        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getAllowedSkewMs() { return allowedSkewMs; }
        public void setAllowedSkewMs(long allowedSkewMs) { this.allowedSkewMs = allowedSkewMs; }
    }
}
