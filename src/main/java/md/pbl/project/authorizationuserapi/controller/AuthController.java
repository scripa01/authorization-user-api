package md.pbl.project.authorizationuserapi.controller;


import lombok.RequiredArgsConstructor;
import md.pbl.project.authorizationuserapi.model.dto.AuthRequest;
import md.pbl.project.authorizationuserapi.model.dto.AuthResponseDto;
import md.pbl.project.authorizationuserapi.model.dto.RegisterDto;
import md.pbl.project.authorizationuserapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto dto) {
        authService.register(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequest dto) {
        AuthResponseDto resp = authService.login(dto);
        return ResponseEntity.ok(resp);
    }
}
