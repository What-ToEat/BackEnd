package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResponse;
import capstone.restaurant.dto.vote.CreateVoteUserRequest;
import capstone.restaurant.dto.vote.CreateVoteUserResponse;
import capstone.restaurant.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CreateVoteUserResponse> createVoteUser(@RequestBody @Validated CreateVoteUserRequest createVoteUserRequest , @PathVariable("id") String voteHash , HttpServletResponse response){

        CreateVoteUserResponse createVoteUserResponse = voteService.createVoteUser(createVoteUserRequest , voteHash);

        Cookie cookie = new Cookie(voteHash , createVoteUserResponse.getUserId().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/vote/" + voteHash);
        cookie.setMaxAge(createVoteUserResponse.getCookieDuration().intValue());

        response.addCookie(cookie);
        return new ResponseDto<>(201 , "Created" ,createVoteUserResponse);
    }
}
