package md.pbl.project.authorizationuserapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.pbl.project.authorizationuserapi.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private Long id;
    private String email;
    private String token;
    private Role role;
}
