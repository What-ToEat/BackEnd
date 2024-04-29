package capstone.restaurant.exceptionHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionStatus {

    VOTE_NOT_FOUND(404 , "없는 투표 입니다"),
    RESTAURANT_NOT_FOUND(404 , "없는 레스토랑 입니다");

    private final Integer statusCode;
    private final String message;
}
