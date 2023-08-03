package myproject.SummerSpringBootProject.exception;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody, automatiskai padarantis response body JSON formatu
//jeigu naudoti tik @ControllerAdvice, reiketu prideti prie kiekvieno exception @ResponseBody
public class ExceptionHandlerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleAuthenticationException(AuthenticationException ex) {
        return new ExceptionModel(Collections.singletonList(ex.getMessage()));
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

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionModel handleUsernameNotFoundException() {
        return new ExceptionModel(Collections.singletonList("Vartotojas nerastas."));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionModel> handleExpiredJwtException() {
        ExceptionModel exceptionModel = new ExceptionModel(Collections.singletonList("Session expired"));
        return new ResponseEntity<>(exceptionModel, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleSignatureException() {
        return new ExceptionModel(Collections.singletonList("JWT token is not valid."));
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleMalformedJwtException() {
        return new ExceptionModel(Collections.singletonList("JWT token is not valid."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionModel handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ExceptionModel(errors);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionModel handleSQLException(SQLException ex){
        if(ex.getErrorCode() == 1062 && Objects.equals(ex.getSQLState(), "23000"))
            return new ExceptionModel(Collections.singletonList("El. paštas jau užimtas."));

        return new ExceptionModel(Collections.singletonList(ex.getMessage()));
    }
}
