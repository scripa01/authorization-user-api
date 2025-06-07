package md.pbl.project.authorizationuserapi.controller;


import lombok.RequiredArgsConstructor;
import md.pbl.project.authorizationuserapi.model.dto.AuthRequest;
import md.pbl.project.authorizationuserapi.model.dto.AuthResponseDto;
import md.pbl.project.authorizationuserapi.model.dto.RegisterDto;
import md.pbl.project.authorizationuserapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/init-admin")
    public ResponseEntity<AuthResponseDto> initAdmin() {
        AuthResponseDto adminAndGetToken = authService.ensureAdminAndGetToken();
        return ResponseEntity.ok(adminAndGetToken);
    }

    @GetMapping("/get-me")
    public ResponseEntity<AuthResponseDto> getUserByToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.getUserByToken(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequest dto) {
        AuthResponseDto resp = authService.login(dto);
        return ResponseEntity.ok(resp);
    }
}
