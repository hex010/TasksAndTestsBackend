package myproject.SummerSpringBootProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadySolvedQuizException extends RuntimeException {
    public AlreadySolvedQuizException(String message) {super(message);}
}
