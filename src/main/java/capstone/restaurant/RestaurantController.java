package capstone.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "restaurants", description = "식당 조회 API")
@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {
    @Operation(summary = "식당 목록 조회", description = "조건에 맞는 식당의 목록을 조회한다.")
    @GetMapping
    public RestaurantListResponse getRestaurantList(@Parameter(example = "홍대", description = "조회하고 싶은 장소") @RequestParam(required = false) String place,
                                                    @Parameter(example = "맛집", description = "조회하고 싶은 태그, 여러 태그를 조회하려면 여러번 쓴다.") @RequestParam(required = false) String[] tags,
                                                    @Parameter(example = "롯데리아", description = "검색하고 싶은 단어") @RequestParam(required = false) String search,
                                                    @Parameter(example = "1", description = "조회하려는 페이지 번호, 기본으로 1") @RequestParam(required = false, defaultValue = "1") int page) {
        return new RestaurantListResponse();
    }

    @Operation(summary = "식당 상세 조회", description = "식당의 상세정보를 조회한다.")
    @GetMapping("/{id}")
    public RestaurantResponse getRestaurant(@PathVariable String id) {
        return new RestaurantResponse();
    }
}
