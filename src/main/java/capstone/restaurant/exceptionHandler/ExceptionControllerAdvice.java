package capstone.restaurant.exceptionHandler;

import capstone.restaurant.controller.RestaurantController;
import capstone.restaurant.dto.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleInternalServerError(EntityNotFoundException exception) {
        ResponseDto<Void> responseDto = new ResponseDto<>(404 , exception.getMessage());
        return ResponseEntity.status(404).body(responseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> IllegalArgumentException(IllegalArgumentException exception){
        ResponseDto<Void> responseDto = new ResponseDto<>(400 , exception.getMessage());
        return ResponseEntity.status(400).body(responseDto);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDto<Void>> IllegalStateException(IllegalArgumentException exception){
        ResponseDto<Void> responseDto = new ResponseDto<>(400 , exception.getMessage());
        return ResponseEntity.status(400).body(responseDto);
    }
}
