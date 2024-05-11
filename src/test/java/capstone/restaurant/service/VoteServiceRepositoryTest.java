package capstone.restaurant.service;


import capstone.restaurant.dto.vote.CreateVoteUserRequest;
import capstone.restaurant.dto.vote.CreateVoteUserResponse;
import capstone.restaurant.dto.vote.FindVoteOptionVoter;
import capstone.restaurant.dto.vote.FindVoteResponse;
import capstone.restaurant.entity.*;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class VoteServiceRepositoryTest {

    @Autowired
    public RestaurantRepository restaurantRepository;
    @Autowired
    public VoteRepository voteRepository;
    @Autowired
    public VoteOptionRepository voteOptionRepository;
    @Autowired
    public VoterRepository voterRepository;
    @Autowired
    public VoteResultRepository voteResultRepository;
    @Autowired
    public VoteService voteService;

    @AfterEach
    public void afterEach(){
        voteResultRepository.deleteAll();
        voteOptionRepository.deleteAll();
        restaurantRepository.deleteAll();
        voteRepository.deleteAll();
        voterRepository.deleteAll();
    }

    @Test
    public void findVoteTest(){

        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        Vote vote =  Vote.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        vote.getVoteOptions().add(voteOption1);
        vote.getVoteOptions().add(voteOption2);

        voteRepository.save(vote);

        voteOptionRepository.save(voteOption1);
        voteOptionRepository.save(voteOption2);

        Voter voter1 = Voter.builder().nickname("aaa").profileImage(1).vote(vote).build();
        Voter voter2 = Voter.builder().nickname("bbb").profileImage(2).vote(vote).build();
        Voter voter3 = Voter.builder().nickname("ccc").profileImage(3).vote(vote).build();

        Voter[] voters = {voter1 , voter2 , voter3};
        voterRepository.saveAll(Arrays.asList(voters));

        VoteResult voteResult1 = VoteResult.builder().voteOption(voteOption1).voter(voter1).build();
        VoteResult voteResult2 = VoteResult.builder().voteOption(voteOption1).voter(voter2).build();
        VoteResult voteResult3 = VoteResult.builder().voteOption(voteOption2).voter(voter3).build();
        VoteResult[] voteResults = {voteResult1 , voteResult2 , voteResult3};
        voteResultRepository.saveAll(Arrays.asList(voteResults));

        FindVoteResponse findVoteResponse = voteService.findVote(vote.getVoteHash());

        List<FindVoteOptionVoter> voterList1 = findVoteResponse.getVoteOptionInfoList().get(0).getVoterList();
        List<FindVoteOptionVoter> voterList2 = findVoteResponse.getVoteOptionInfoList().get(1).getVoterList();

        Assertions.assertThat(voterList1.size()).isEqualTo(2);
        Assertions.assertThat(voterList2.size()).isEqualTo(1);

        Assertions.assertThat(findVoteResponse.getVoteOptionInfoList().size()).isEqualTo(2);

    }

    @Test
    public void voteParticipateTest_SUCCESS(){

        Vote vote =  Vote.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        this.voteRepository.save(vote);

        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("abcd");
        createVoteUserRequest.setUserImage(1);

        CreateVoteUserResponse voteUser = this.voteService.createVoteUser(createVoteUserRequest, vote.getVoteHash());

        Assertions.assertThat(voteUser.getNickname()).isEqualTo("abcd");
        Assertions.assertThat(voteUser.getProfileImage()).isEqualTo(1);
        Assertions.assertThat(voteUser.getUserId()).isInstanceOf(Long.class);
    }

    @Test
    public void voteParticipateTest_SUCCESS_alreadyExists(){
        Vote vote =  Vote.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        this.voteRepository.save(vote);

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        this.voterRepository.save(voter);

        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("abcd");
        createVoteUserRequest.setUserImage(1);

        CreateVoteUserResponse voteUser = this.voteService.createVoteUser(createVoteUserRequest, vote.getVoteHash());

        Assertions.assertThat(voteUser.getNickname()).isEqualTo("abcd");
        Assertions.assertThat(voteUser.getProfileImage()).isEqualTo(1);
        Assertions.assertThat(voteUser.getUserId()).isInstanceOf(Long.class);

    }

    @Test
    public void voteParticipateTest_FAIL(){
        Vote vote =  Vote.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        this.voteRepository.save(vote);


        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("abcd");
        createVoteUserRequest.setUserImage(1);

        Assertions.assertThatThrownBy(() -> {
            this.voteService.createVoteUser(createVoteUserRequest, "abcde");
        }).isInstanceOf(EntityNotFoundException.class);

    }
}
