package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.vote.*;
import capstone.restaurant.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "vote", description = "투표 관련 api")
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;

    @Operation(summary = "투표 생성", description = "투표를 생성한다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CreateVoteResponse> createVote(@RequestBody @Validated CreateVoteRequest createVoteRequest) {
        CreateVoteResponse createVoteResponse = voteService.createVote(createVoteRequest);
        return new ResponseDto<>(201, "Created", createVoteResponse);
    }

    @Operation(summary = "투표 참여", description = "투표에 참여한다.")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "201" , description = "레스토랑 투표 참여") , @ApiResponse(responseCode = "404" , description = "식당을 찾을 수 없음")})
    public ResponseDto<CreateVoteUserResponse> createVoteUser(@RequestBody @Validated CreateVoteUserRequest createVoteUserRequest , @PathVariable("id") String voteHash , HttpServletResponse response){

        CreateVoteUserResponse createVoteUserResponse = voteService.createVoteUser(createVoteUserRequest, voteHash);
        return new ResponseDto<>(200 , "투표 참여 완료" ,createVoteUserResponse);
    }

    @Operation(summary = "투표 하기", description = "투표의 선택지를 고른다.")
    @PostMapping("/{voteHash}/selection")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> createVoteResult(
            @PathVariable("voteHash") String voteHash,
            @RequestBody CreateVoteResultRequest createVoteResultRequest
    ) {
        voteService.createVoteResult(voteHash, createVoteResultRequest);
        return new ResponseDto<>(201, "Created", null);
    }

    @Operation(summary = "투표 취소", description = "사용자의 투표 내용을 전부 지운다. 사용자가 투표 한적이 없더라도 이 요청을 보내면 오류가 발생하지 않는다. 항상 사용자 투표를 하든 안했던 안한 상태로 만든다.")
    @DeleteMapping("/{voteHash}/selection")
    public ResponseDto<Object> deleteVoteResult(
            @PathVariable("voteHash") String voteHash,
            @RequestBody DeleteVoteResultRequest deleteVoteResultRequest
    ) {
        voteService.deleteVoteResult(voteHash, deleteVoteResultRequest);
        return new ResponseDto<>(200, "Ok", null);
    }

    @Operation(summary = "투표 조회" , description = "투표를 조회한다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "200" , description = "투표 조회 성공") , @ApiResponse(responseCode = "404" , description = "투표를 찾을 수 없음")})
    public ResponseDto<FindVoteResponse> findVote(@PathVariable("id") String voteId){
        FindVoteResponse findVoteResponse = this.voteService.findVote(voteId);
        return new ResponseDto<>(200 , "ok" , findVoteResponse);
    }
}
