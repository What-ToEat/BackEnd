package capstone.restaurant.controller;

import capstone.restaurant.dto.GetMemberReturnDto;
import capstone.restaurant.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "vote" , description = "투표 관련 api")
@RestController
@RequestMapping("/vote")
public class VoteController {

    public VoteController(){}

    @Operation(summary = "Swagger 예시" , description = "Swagger 예시")
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "vote 조회 성공") , @ApiResponse(responseCode = "404" , description = "없음")})
    @GetMapping(value = "/example")
    public ResponseEntity<ResponseDto<GetMemberReturnDto>> getMember(@RequestParam(required = false) @Parameter String username , @RequestParam(required = false) @Parameter Integer age){

        if(age % 2 == 0){
            throw new EntityNotFoundException("해당 엔티티가 없습니다");
        }
        ResponseDto<GetMemberReturnDto> responseDto = new ResponseDto<>(201 , "CREATED" , new GetMemberReturnDto("132" , 1));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value = "/exampl")
    public GetMemberReturnDto getMembe(@RequestParam(required = false) @Parameter String username , @RequestParam(required = false) @Parameter Integer age){
        throw new IllegalStateException();
    }
}
