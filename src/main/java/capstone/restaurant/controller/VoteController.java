package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResponse;
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
}
