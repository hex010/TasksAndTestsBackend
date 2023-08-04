package myproject.SummerSpringBootProject.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionModel {
    private String message;
    private List<String> errors;
}
