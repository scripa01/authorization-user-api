package md.pbl.project.authorizationuserapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String email;
    @Column(nullable = false)
    private String password;
    private String role;
}
