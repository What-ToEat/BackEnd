package capstone.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto <T> {

    private Integer statusCode;
    private String message;
    private T data;

    public ResponseDto(Integer statusCode , String message){
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public ResponseDto(Integer statusCode , String message , T data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
