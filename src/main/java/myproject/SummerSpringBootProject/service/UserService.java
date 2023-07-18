package myproject.SummerSpringBootProject.service;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.dtos.UserProfileDTO;
import myproject.SummerSpringBootProject.entity.User;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserProfileDTO getUserProfile(String authToken) {
        String userEmail = jwtService.extractUsernameFromToken(jwtService.getTokenFromHeader(authToken));
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        return UserProfileDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .gender(user.getGender()).build();
    }

    public void updateUserProfile(String authToken, UserProfileDTO myProfileData) {
        String userEmail = jwtService.extractUsernameFromToken(jwtService.getTokenFromHeader(authToken));
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        user.setFirstname(myProfileData.getFirstname());
        user.setLastname(myProfileData.getLastname());
        user.setGender(myProfileData.getGender());

        userRepository.save(user);
    }
}
