package capstone.restaurant.service;

import capstone.restaurant.dto.vote.*;
import capstone.restaurant.entity.*;
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
import java.util.*;

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

        Vote vote = this.voteRepository.findByVoteHash(voteHash);

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

    @Transactional
    public FindVoteResponse findVote(String voteId){
        Vote vote = this.voteRepository.findByVoteHash(voteId);

        if(vote == null) throw new EntityNotFoundException("없는 투표입니다");

        return FindVoteResponse.builder()
                .title(vote.getTitle())
                .allowDuplicateVote(vote.getAllowDuplicateVote())
                .expireAt(vote.getExpireAt())
                .voteHash(vote.getVoteHash())
                .voteOptionInfoList(convertVoteEntityToOptionSub(vote))
                .build();
    }

    private List<FindVoteOptionSub> convertVoteEntityToOptionSub(Vote vote){

        List<FindVoteOptionSub> findVoteOptionSubList = new ArrayList<>();

        for (VoteOption voteOption : vote.getVoteOptions()) {
            FindVoteOptionSub findVoteOptionSub = FindVoteOptionSub.builder()
                    .restaurantName(voteOption.getRestaurant().getName())
                    .restaurantId(voteOption.getRestaurant().getRestaurantHash())
                    .voterList(getVotersForRestaurant(voteOption.getVoteResults()))
                    .build();

            findVoteOptionSubList.add(findVoteOptionSub);
        }

        return findVoteOptionSubList;
    }

    private List<FindVoteOptionVoter> getVotersForRestaurant(List<VoteResult> voteResultsList){
        List<FindVoteOptionVoter> voteOptionSubList = new ArrayList<>();

        for (VoteResult voteResult : voteResultsList) {
            FindVoteOptionVoter findVoteOptionVoter = FindVoteOptionVoter.builder()
                    .userId(voteResult.getVoter().getId())
                    .userImage(voteResult.getVoter().getProfileImage())
                    .nickname(voteResult.getVoter().getNickname())
                    .build();

            voteOptionSubList.add(findVoteOptionVoter);
        }

        return voteOptionSubList;
    }

}
