package capstone.restaurant.service;

import capstone.restaurant.dto.vote.*;
import capstone.restaurant.entity.*;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoterRepository voterRepository;
    private final VoteResultRepository voteResultRepository;

    @Transactional
    public CreateVoteResponse createVote(CreateVoteRequest createVoteRequest) {
        Vote vote = createVoteRequest.toVoteEntity();
        Vote createdVote = voteRepository.save(vote);

        registerVoteOption(createVoteRequest.getRestaurants(), createdVote);
        return new CreateVoteResponse(createdVote.getVoteHash());
    }

    private void registerVoteOption(List<String> restaurants, Vote vote) {
        restaurants.forEach(restaurantHash -> {
            Restaurant restaurant = restaurantRepository.findByRestaurantHash(restaurantHash);
            if (restaurant == null) {
                throw new EntityNotFoundException(restaurantHash + " Not Found");
            }
            VoteOption voteOption = VoteOption.builder()
                    .vote(vote)
                    .restaurant(restaurant)
                    .build();
            voteOptionRepository.save(voteOption);
        });
    }

    public CreateVoteUserResponse createVoteUser(CreateVoteUserRequest createVoteUserRequest , String voteHash){

        Vote vote = this.voteRepository.findVoteByVoteHash(voteHash);

        if(vote == null){
            throw new EntityNotFoundException("없는 투표입니다");
        }

        Long cookieExpireAt = Duration.between(LocalDateTime.now() , vote.getExpireAt()).getSeconds();


        Voter voter = Voter.builder()
                .nickname(createVoteUserRequest.getUserName())
                .profileImage(createVoteUserRequest.getUserImage())
                .vote(vote)
                .build();

        this.voterRepository.save(voter);

        CreateVoteUserResponse createVoteUserResponse = new CreateVoteUserResponse();
        createVoteUserResponse.setUserId(voter.getId());
        createVoteUserResponse.setCookieDuration(cookieExpireAt);

        return createVoteUserResponse;
    }

    public void createVoteResult(String voteHash, CreateVoteResultRequest createVoteResultRequest) {
        Long userId = createVoteResultRequest.getUserId();
        List<String> options = createVoteResultRequest.getOptions();

        Vote vote = this.voteRepository.findVoteByVoteHash(voteHash);
        if(vote == null){
            throw new EntityNotFoundException("없는 투표 입니다.");
        }

        Optional<Voter> voter = voterRepository.findById(userId);
        if (voter.isEmpty() || !Objects.equals(voter.get().getVote().getVoteHash(), voteHash)) {
            throw new EntityNotFoundException("없는 사용자 입니다.");
        }

        vote.getVoters().forEach(voter1 -> {
            if (!voter1.getVoteResult().isEmpty() && Objects.equals(voter1.getId(), userId)) {
                throw new IllegalArgumentException("이미 투표 했습니다.");
            }
        });

        if (!vote.getAllowDuplicateVote() && options.size() >= 2) {
            throw new IllegalArgumentException("중복 투표가 아닌데 2개 이상의 옵션을 골랐습니다.");
        }
        List<VoteResult> voteResults = new ArrayList<>();
        options.forEach(option -> {
            vote.getVoteOptions().forEach(voteOption -> {
                if (Objects.equals(voteOption.getRestaurant().getRestaurantHash(), option)) {
                    VoteResult voteResult = VoteResult.builder()
                            .voter(voter.get())
                            .voteOption(voteOption)
                            .build();
                    voteResults.add(voteResult);
                }
            });
        });

        voteResultRepository.saveAll(voteResults);
    }
}
