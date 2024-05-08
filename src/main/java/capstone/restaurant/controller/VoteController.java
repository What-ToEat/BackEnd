package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResponse;
import capstone.restaurant.dto.vote.CreateVoteUserRequest;
import capstone.restaurant.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<> createVoteUser(@RequestBody @Validated CreateVoteUserRequest createVoteUserRequest , @PathVariable("id") String voteId){
        voteService.createVoteUser(createVoteUserRequest , voteId);
        return new ResponseDto<>(201 , "Created");
    }
}
