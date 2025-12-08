package chill_logistics.user_server.infrastructure.repository;

import chill_logistics.user_server.domain.entity.SignupStatus;
import chill_logistics.user_server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findBySignupStatus(SignupStatus signupStatus);
}
