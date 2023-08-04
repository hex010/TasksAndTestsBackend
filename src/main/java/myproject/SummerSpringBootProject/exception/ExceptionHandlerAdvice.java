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

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody, automatiskai padarantis response body JSON formatu
//jeigu naudoti tik @ControllerAdvice, reiketu prideti prie kiekvieno exception @ResponseBody
public class ExceptionHandlerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleAuthenticationException(AuthenticationException ex) {
        return new ExceptionModel(ex.getMessage(), List.of());
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
        return new ExceptionModel("Vartotojas nerastas", List.of());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionModel> handleExpiredJwtException() {
        ExceptionModel exceptionModel = new ExceptionModel("Sesija baigėsi. Prisijunkite dar kartą.", List.of());
        return new ResponseEntity<>(exceptionModel, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleSignatureException() {
        //jwt token is not valid
        return new ExceptionModel("Įvyko sesijos klaida", List.of());
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ExceptionModel handleMalformedJwtException() {
        //jwt token is not valid
        return new ExceptionModel("Įvyko sesijos klaida", List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionModel handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ExceptionModel("", errors);
    }
}
