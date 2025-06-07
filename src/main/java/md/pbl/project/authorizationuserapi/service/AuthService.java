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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final OrganizationProjectUserClient orgClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_RAW_PASSWORD = "veryStrongPassword2025!";

    public AuthResponseDto register(RegisterDto dto) {
        List<UserDto> existing = orgClient.findByUsername(dto.getOrgId(), dto.getUsername());
        if (!existing.isEmpty()) {
            throw new RuntimeException("User already exists in organisation-project-user-api");
        }

        if (authUserRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User already registered");
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
        authUserRepository.save(authUser);

        CreateUserDto createUser = new CreateUserDto(dto.getUsername(), dto.getEmail(), dto.getFullName(), dto.getRole());
        orgClient.createUser(dto.getOrgId(), createUser);

        String token = jwtUtil.generateToken(authUser);
        return new AuthResponseDto(authUser.getId(), authUser.getEmail(), token, dto.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(AuthRequest request) {
        AuthUser user = authUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new AuthResponseDto(user.getId(), user.getEmail(), jwtUtil.generateToken(user), user.getRole());
    }

    @Transactional
    public AuthResponseDto ensureAdminAndGetToken() {
        Optional<AuthUser> optAdmin = authUserRepository.findByUsername(ADMIN_USERNAME);

        AuthUser admin;
        if (optAdmin.isEmpty()) {
            admin = AuthUser.builder()
                    .username(ADMIN_USERNAME)
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_RAW_PASSWORD))
                    .role(Role.ADMIN)
                    .build();
            authUserRepository.save(admin);
        } else {
            admin = optAdmin.get();
        }

        return new AuthResponseDto(admin.getId(), admin.getEmail(), jwtUtil.generateToken(admin), admin.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponseDto getUserByToken(String token) {
        String username = jwtUtil.extractUsername(token);
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User by UserName not found"));
        return new AuthResponseDto(authUser.getId(), authUser.getEmail(), token, authUser.getRole());
    }
}