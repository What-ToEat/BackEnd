package capstone.restaurant.service;

import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResponse;
import capstone.restaurant.entity.Vote;
import capstone.restaurant.repository.VoteOptionRepository;
import capstone.restaurant.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;

    @Transactional
    public CreateVoteResponse createVote(CreateVoteRequest createVoteRequest) {
        Vote vote = createVoteRequest.toVoteEntity();
        Vote createdVote = voteRepository.save(vote);
        return new CreateVoteResponse(createdVote.getVoteHash());
    }

}
