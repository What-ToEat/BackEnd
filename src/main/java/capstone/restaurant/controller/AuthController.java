package capstone.restaurant.controller;

import capstone.restaurant.dto.auth.LoginKakaoRequest;
import capstone.restaurant.dto.auth.Tokens;
import capstone.restaurant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/kakao")
    public Tokens loginKakao(@RequestBody @Validated LoginKakaoRequest request) {
        return authService.signupOrLogin(request.getToken());
    }
}
