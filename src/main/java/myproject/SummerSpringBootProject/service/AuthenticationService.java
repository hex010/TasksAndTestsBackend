package myproject.SummerSpringBootProject.service;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.dtos.LoginRequest;
import myproject.SummerSpringBootProject.dtos.LoginResponse;
import myproject.SummerSpringBootProject.dtos.RegisterRequest;
import myproject.SummerSpringBootProject.entity.User;
import myproject.SummerSpringBootProject.enums.Gender;
import myproject.SummerSpringBootProject.enums.Role;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public LoginResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .gender(Gender.UNDISCLOSED)
                .accountNonLocked(true)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder().token(jwtToken).build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder().token(jwtToken).build();
    }
}
