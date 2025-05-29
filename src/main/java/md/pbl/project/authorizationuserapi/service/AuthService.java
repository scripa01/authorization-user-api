package md.pbl.project.authorizationuserapi.service;

import lombok.RequiredArgsConstructor;
import md.pbl.project.authorizationuserapi.client.OrganizationProjectUserClient;
import md.pbl.project.authorizationuserapi.model.AuthUser;
import md.pbl.project.authorizationuserapi.model.Role;
import md.pbl.project.authorizationuserapi.model.dto.*;
import md.pbl.project.authorizationuserapi.repository.AuthUserRepository;
import md.pbl.project.authorizationuserapi.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final OrganizationProjectUserClient orgClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDto register(RegisterDto dto) {
        // 1) проверить в org-сервисе
        List<UserDto> existing = orgClient.findByUsername(dto.getOrgId(), dto.getUsername());
        if (!existing.isEmpty()) {
            throw new RuntimeException("User already exists in organisation-project-user-api");
        }
        // 2) проверить в собственном репозитории
        if (authUserRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User already registered");
        }
        // 3) сохранить credentials
        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
        authUserRepository.save(authUser);
        // 4) создать профиль в org-сервисе
        CreateUserDto createUser = new CreateUserDto(dto.getUsername(), dto.getEmail(), String.valueOf(dto.getRole()), dto.getName());
        orgClient.createUser(dto.getOrgId(), createUser);
        // 5) вернуть токен
        String token = jwtUtil.generateToken(authUser.getEmail());
        return new AuthResponseDto(token, Role.valueOf(dto.getRole()));
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(AuthRequest request) {
        AuthUser user = authUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new AuthResponseDto(jwtUtil.generateToken(user.getEmail()), Role.valueOf(user.getRole()));
    }
}