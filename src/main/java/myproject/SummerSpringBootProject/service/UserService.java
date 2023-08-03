package myproject.SummerSpringBootProject.service;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.dtos.UserProfileDTO;
import myproject.SummerSpringBootProject.entity.User;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProfileDTO getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("Vartotojas nerastas"));

        return UserProfileDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .gender(user.getGender()).build();
    }

    public void updateUserProfile(UserProfileDTO myProfileData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow();

        user.setFirstname(myProfileData.getFirstname());
        user.setLastname(myProfileData.getLastname());
        user.setGender(myProfileData.getGender());

        userRepository.save(user);
    }
}
