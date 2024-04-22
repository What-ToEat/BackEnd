package capstone.restaurant.vote;

import capstone.restaurant.vote.dto.GetMemberReturnDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

@Tag(name = "vote" , description = "투표 관련 api")
@RestController
@RequestMapping("/vote")
public class VoteController {

    public VoteController(){}

    @Operation(summary = "Swagger 예시" , description = "Swagger 예시")
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "vote 조회 성공" , content = @Content(schema = @Schema(implementation = GetMemberReturnDto.class))) , @ApiResponse(responseCode = "403" , description = "Forbidden" , content = @Content(schema = @Schema()))})
    @GetMapping(value = "/example")
    public GetMemberReturnDto getMember(@RequestParam(required = false) @Parameter String username , @RequestParam(required = false) @Parameter Integer age){
        return new GetMemberReturnDto("123" , 12 );
    }

    @GetMapping(value = "/exampl")
    public GetMemberReturnDto getMembe(@RequestParam(required = false) @Parameter String username , @RequestParam(required = false) @Parameter Integer age){
        throw new IllegalStateException();
    }
}
