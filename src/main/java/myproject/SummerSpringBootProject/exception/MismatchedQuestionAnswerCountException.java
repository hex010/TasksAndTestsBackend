package myproject.SummerSpringBootProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MismatchedQuestionAnswerCountException extends RuntimeException {
    public MismatchedQuestionAnswerCountException(String message) {
        super(message);
    }
}