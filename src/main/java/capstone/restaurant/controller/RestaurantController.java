package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "restaurant" , description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    public RestaurantController(){}

    @Operation(summary = "Swagger 예시" , description = "Swagger 예시")
    public ResponseEntity<ResponseDto<List<>>> findRestaurantList(){

    }
}
