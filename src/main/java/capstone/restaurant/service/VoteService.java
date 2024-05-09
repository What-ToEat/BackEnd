package capstone.restaurant.service;

import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResponse;
import capstone.restaurant.dto.vote.CreateVoteUserRequest;
import capstone.restaurant.dto.vote.CreateVoteUserResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.Vote;
import capstone.restaurant.entity.VoteOption;
import capstone.restaurant.entity.Voter;
import capstone.restaurant.repository.RestaurantRepository;
import capstone.restaurant.repository.VoteOptionRepository;
import capstone.restaurant.repository.VoteRepository;
import capstone.restaurant.repository.VoterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoterRepository voterRepository;

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
        Long cookieExpireAt = Duration.between(LocalDateTime.now() , vote.getExpireAt()).getSeconds();

        if(vote == null){
            throw new EntityNotFoundException("없는 투표입니다");
        }

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

}
