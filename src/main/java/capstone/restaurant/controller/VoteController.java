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

import java.util.Map;

@Tag(name = "vote" , description = "투표 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;
    @Operation(summary = "투표 생성" , description = "투표를 생성한다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CreateVoteResponse> createVote(@RequestBody @Validated CreateVoteRequest createVoteRequest) {
        CreateVoteResponse createVoteResponse = voteService.createVote(createVoteRequest);
        return new ResponseDto<>(201, "Created", createVoteResponse);
    }

    @Operation(summary = "투표 참여" , description = "투표에 참여한다.")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = { @ApiResponse(responseCode = "201" , description = "레스토랑 투표 참여") , @ApiResponse(responseCode = "404" , description = "식당을 찾을 수 없음")})
    public ResponseDto<CreateVoteUserResponse> createVoteUser(@RequestBody @Validated CreateVoteUserRequest createVoteUserRequest , @PathVariable("id") String voteHash , HttpServletResponse response){

        CreateVoteUserResponse createVoteUserResponse = voteService.createVoteUser(createVoteUserRequest , voteHash);

        return new ResponseDto<>(200 , "투표 참여 완료" ,createVoteUserResponse);
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
