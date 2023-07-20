package myproject.SummerSpringBootProject.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.JMException;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    ExceptionModel handleAuthenticationException(AuthenticationException ex) {
        return new ExceptionModel(ex.getMessage());
    }
    /*
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Object> handleAccessDeniedException(AuthenticationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
    */

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionModel> handleExpiredJwtException() {
        ExceptionModel exceptionModel = new ExceptionModel("Session expired");
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JMException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionModel handleJMException() {
        return new ExceptionModel("Incorrect JWT token.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionModel handleMalformedJwtException() {
        return new ExceptionModel("JWT token is not valid.");
    }
}
