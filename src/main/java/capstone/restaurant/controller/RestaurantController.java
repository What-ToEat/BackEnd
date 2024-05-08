package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.restaurant.RestaurantListResponse;
import capstone.restaurant.dto.restaurant.RestaurantResponse;
import capstone.restaurant.dto.tag.TagListResponse;
import capstone.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@Tag(name = "restaurants", description = "식당 조회 API")
@RestController
@RequestMapping("api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {


    private final RestaurantService restaurantService;

    @Operation(summary = "식당 목록 조회", description = "조건에 맞는 식당의 목록을 조회한다.")
    @GetMapping("/tag")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "태그로 레스토랑 리스트 조회 성공") , @ApiResponse(responseCode = "400" , description = "page 는 1 이상의 정수이어야함")})
    public ResponseDto<RestaurantListResponse> getRestaurantListByTag(@Parameter(example = "홍대", description = "조회하고 싶은 장소") @RequestParam(required = false) String place,
                                                                 @Parameter(example = "맛집", description = "조회하고 싶은 태그, 여러 태그를 조회하려면 여러번 쓴다.") @RequestParam(required = false) String[] tags,
                                                                 @Parameter(example = "1", description = "조회하려는 페이지 번호, 기본으로 1") @RequestParam(required = false, defaultValue = "1") int page) {

        if(page <= 0) throw new IllegalArgumentException("page 는 0보다 큰 정수이어야 합니다");

        RestaurantListResponse restaurantListResponse = this.restaurantService.restaurantListFindByTag(place , tags  , page);

        return new ResponseDto<>(200, "ok", restaurantListResponse);

    }

    @Operation(summary = "식당 검색 조회", description = "검색어로 식당을 조회한다.")
    @GetMapping("/keyword")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "키워드로 레스토랑 리스트 조회 성공") , @ApiResponse(responseCode = "400" , description = "page 는 1 이상의 정수이어야함" )})
    public ResponseDto<RestaurantListResponse> getRestaurantListByKeyword(@Parameter(example = "롯데리아" , description = "검색 키워드") @RequestParam(required = false) String keyword,
                                                                          @Parameter(example = "1", description = "조회하려는 페이지 번호, 기본으로 1") @RequestParam(required = false, defaultValue = "1") int page){

        if(page <= 0) throw new IllegalArgumentException("page 는 0보다 큰 정수이어야 합니다");

        RestaurantListResponse restaurantListResponse=  this.restaurantService.restaurantListFindByKeyword(keyword , page);

        return new ResponseDto<>(200, "ok" , restaurantListResponse);
    }

    @Operation(summary = "식당 상세 조회", description = "식당의 상세정보를 조회한다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "id 로 상세 레스토랑 정보 반환") , @ApiResponse(responseCode = "404" , description = "식당을 찾을 수 없음")})
    public ResponseDto<RestaurantResponse> getRestaurant(@PathVariable String id) {
        RestaurantResponse restaurantResponse = this.restaurantService.restaurantFindById(id);
        return new ResponseDto<>(200, "ok", restaurantResponse);
    }
}
