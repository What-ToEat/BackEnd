package capstone.restaurant.service;


import capstone.restaurant.dto.vote.*;
import capstone.restaurant.entity.*;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .email(null)
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
                .email(null)
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
                .email(null)
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
                .email(null)
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

    @Test
    @Transactional
    @DisplayName("[createVoteResult] 투표 결과 저장 성공")
    public void createVoteResultSuccessTest(){

        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        this.restaurantRepository.save(restaurant1);
        this.restaurantRepository.save(restaurant2);

        Vote vote =  Vote.builder()
                .title("Test")
                .email(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        Voter voter1 = this.voterRepository.save(voter);

        vote.getVoters().add(voter1);

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        vote.getVoteOptions().add(voteOption1);
        vote.getVoteOptions().add(voteOption2);

        this.voteRepository.save(vote);

        voteOptionRepository.save(voteOption1);
        voteOptionRepository.save(voteOption2);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter1.getId())
                .nickname("abcd")
                .options(op)
                .build();

        this.voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);

        Vote vote1 = this.voteRepository.findByVoteHash(vote.getVoteHash());
        int voteResult = 0;
        
        for (VoteOption voteOption : vote1.getVoteOptions()) {
            System.out.println("voteOption.getVoteResults() = " + voteOption.getVoteResults());
            voteResult += voteOption.getVoteResults().size();
        }

        Assertions.assertThat(voteResult).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("[createVoteResult] 중복 투표가 아닌데 중복 투표하면 실패")
    public void duplicateVoteFailTest(){

        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        this.restaurantRepository.save(restaurant1);
        this.restaurantRepository.save(restaurant2);

        Vote vote =  Vote.builder()
                .title("Test")
                .email(null)
                .allowDuplicateVote(false)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        Voter voter1 = this.voterRepository.save(voter);

        vote.getVoters().add(voter1);

        this.voteRepository.save(vote);

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        vote.getVoteOptions().add(voteOption1);
        vote.getVoteOptions().add(voteOption2);

        voteOptionRepository.save(voteOption1);
        voteOptionRepository.save(voteOption2);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter1.getId())
                .nickname("abcd")
                .options(op)
                .build();

        Assertions.assertThatThrownBy(() -> {
            voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("[createVoteResult] 이미 투표했는데 투표 요청 오면 실패")
    public void alreadyVotedFailTest(){

        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        this.restaurantRepository.save(restaurant1);
        this.restaurantRepository.save(restaurant2);

        Vote vote =  Vote.builder()
                .title("Test")
                .email(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdef")
                .build();

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        Voter voter1 = this.voterRepository.save(voter);

        vote.getVoters().add(voter1);

        this.voteRepository.save(vote);

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        vote.getVoteOptions().add(voteOption1);
        vote.getVoteOptions().add(voteOption2);

        voteOptionRepository.save(voteOption1);
        voteOptionRepository.save(voteOption2);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter1.getId())
                .nickname("abcd")
                .options(op)
                .build();

        VoteResult voteResult = VoteResult.builder().voteOption(voteOption1).voter(voter).build();

        voter1.getVoteResult().add(voteResult);

        Assertions.assertThatThrownBy(() -> {
            voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("[checkIsExpired] 만료된 투표에 투표하면 실패")
    public void voteTimePassedFailTest(){

        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        this.restaurantRepository.save(restaurant1);
        this.restaurantRepository.save(restaurant2);

        Vote vote =  Vote.builder()
                .title("Test")
                .email(null)
                .allowDuplicateVote(true)
                .expireAt(LocalDateTime.now().minusHours(2))
                .voteHash("abcdef")
                .build();

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        vote.getVoters().add(voter);

        this.voteRepository.save(vote);

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        vote.getVoteOptions().add(voteOption1);
        vote.getVoteOptions().add(voteOption2);

        voteOptionRepository.save(voteOption1);
        voteOptionRepository.save(voteOption2);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter.getId())
                .nickname("abcd")
                .options(op)
                .build();

        Assertions.assertThatThrownBy(() -> {
            voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalStateException.class);
    }
}
