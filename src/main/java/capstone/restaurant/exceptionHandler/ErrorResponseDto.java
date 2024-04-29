package capstone.restaurant.exceptionHandler;


public class ErrorResponseDto{

    private Integer statusCode;
    private String message;
    public ErrorResponseDto(Integer statusCode , String message){
        this.statusCode = statusCode;
        this.message = message;
    }
}
