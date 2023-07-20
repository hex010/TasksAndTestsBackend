package myproject.SummerSpringBootProject.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Size(min = 7, max = 70, message = "El. paštas turi būti 7-70 simbolių")
    @Email(message = "Netinkamas el. pašto formatas")
    private String email;

    @NotBlank(message = "Slaptažodis negali būti tuščias.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Slaptažodis turi turėti savyje bent 1 raidę ir bent 1 simbolį")
    private String password;

    @Size(min = 2, max = 50, message = "Vardas turi būti 2-50 simbolių")
    private String firstname;

    @Size(min = 2, max = 50, message = "Pavardė turi būti 2-50 simbolių")
    private String lastname;
}
