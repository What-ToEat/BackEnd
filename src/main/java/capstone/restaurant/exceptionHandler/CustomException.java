package capstone.restaurant.exceptionHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    private ExceptionStatus exceptionStatus;
}
