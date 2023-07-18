package myproject.SummerSpringBootProject.controller;

import lombok.AllArgsConstructor;
import myproject.SummerSpringBootProject.dtos.UserProfileDTO;
import myproject.SummerSpringBootProject.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RequestMapping("/api/v1/user")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken){
        return ResponseEntity.ok(userService.getUserProfile(authToken));
    }
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfileDTO myProfileData, @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        userService.updateUserProfile(authToken, myProfileData);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
