package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.enums.Gender;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String email;
    private String firstname;
    private String lastname;
    private Gender gender;
}