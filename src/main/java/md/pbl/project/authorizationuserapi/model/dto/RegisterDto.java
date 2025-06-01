package md.pbl.project.authorizationuserapi.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.pbl.project.authorizationuserapi.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private Long orgId;
    @NotNull
    private String username;
    private String fullName;
    @NotNull
    private String email;
    @NotNull

    private String password;
    @NotNull
    private Role role;
}
