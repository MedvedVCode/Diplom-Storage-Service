package medved.java.back.advice;

import medved.java.back.dto.ErrorDto;
import medved.java.back.exception.ActionDataException;
import medved.java.back.exception.InputDataException;
import medved.java.back.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ErrorDto> handleInputData(InputDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), 400));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage(), 401));
    }

    @ExceptionHandler(ActionDataException.class)
    public ResponseEntity<ErrorDto> handleActionData(ActionDataException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage(), 500));
    }
}
