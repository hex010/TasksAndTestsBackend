package myproject.SummerSpringBootProject.controller;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.dtos.LoginRequest;
import myproject.SummerSpringBootProject.dtos.LoginResponse;
import myproject.SummerSpringBootProject.dtos.RegisterRequest;
import myproject.SummerSpringBootProject.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }
}
