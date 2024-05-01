package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.RestaurantReturnDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "restaurant" , description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    public RestaurantController(){}

    @Operation(summary = "Swagger 예시" , description = "Swagger 예시")
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "레스토랑 조회 완료") , @ApiResponse(responseCode = "404" , description = "없음")})
    @GetMapping()
    public ResponseEntity<ResponseDto<List<RestaurantReturnDto>>> findRestaurantList(){
        ResponseDto<List<RestaurantReturnDto>> returnDto = new ResponseDto<>(200 , "123");
        return ResponseEntity.ok(returnDto);
    }
}
