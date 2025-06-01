package md.pbl.project.authorizationuserapi.repository;


import md.pbl.project.authorizationuserapi.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);

    boolean existsByEmail(String email);
}
