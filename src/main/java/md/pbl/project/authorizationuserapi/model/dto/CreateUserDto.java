package md.pbl.project.authorizationuserapi.model.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.pbl.project.authorizationuserapi.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String username;
    @Email
    private String email;
    private String fullName;
    private Role role;
}
