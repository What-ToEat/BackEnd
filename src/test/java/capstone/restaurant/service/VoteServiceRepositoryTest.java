package capstone.restaurant.service;


import capstone.restaurant.dto.vote.*;
import capstone.restaurant.entity.*;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    public EntityManager entityManager;

    @BeforeEach
    public void setData(){

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

        Vote vote1 =  Vote.builder()
                .title("Test")
                .email(null)
                .allowDuplicateVote(false)
                .expireAt(LocalDateTime.now().plusHours(2))
                .voteHash("abcdefgh")
                .build();

        VoteOption voteOption1 = VoteOption.builder().restaurant(restaurant1).vote(vote).build();
        VoteOption voteOption2 = VoteOption.builder().restaurant(restaurant2).vote(vote).build();

        VoteOption voteOption3 = VoteOption.builder().restaurant(restaurant1).vote(vote1).build();
        VoteOption voteOption4 = VoteOption.builder().restaurant(restaurant2).vote(vote1).build();

        this.voteRepository.save(vote);
        this.voteRepository.save(vote1);

        this.voteOptionRepository.save(voteOption1);
        this.voteOptionRepository.save(voteOption2);

        this.voteOptionRepository.save(voteOption3);
        this.voteOptionRepository.save(voteOption4);

        Voter voter1 = Voter.builder().nickname("aaa").profileImage(1).vote(vote).build();
        Voter voter2 = Voter.builder().nickname("bbb").profileImage(2).vote(vote).build();
        Voter voter3 = Voter.builder().nickname("ccc").profileImage(3).vote(vote).build();

        this.voterRepository.save(voter1);
        this.voterRepository.save(voter2);
        this.voterRepository.save(voter3);

        VoteResult voteResult1 = VoteResult.builder().voteOption(voteOption1).voter(voter1).build();
        VoteResult voteResult2 = VoteResult.builder().voteOption(voteOption1).voter(voter2).build();
        VoteResult voteResult3 = VoteResult.builder().voteOption(voteOption2).voter(voter3).build();

        VoteResult[] voteResults = {voteResult1 , voteResult2 , voteResult3};

        voteResultRepository.saveAll(Arrays.asList(voteResults));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    @DisplayName("[findVote] 투표 찾기 기능 성공")
    public void findVoteTest(){

        FindVoteResponse findVoteResponse = this.voteService.findVote("abcdef");

        List<FindVoteOptionVoter> voterList1 = findVoteResponse.getVoteOptionInfoList().get(0).getVoterList();
        List<FindVoteOptionVoter> voterList2 = findVoteResponse.getVoteOptionInfoList().get(1).getVoterList();

        Assertions.assertThat(voterList1.size()).isEqualTo(2);
        Assertions.assertThat(voterList2.size()).isEqualTo(1);

        Assertions.assertThat(findVoteResponse.getVoteOptionInfoList().size()).isEqualTo(2);

    }

    @Test
    @Transactional
    @DisplayName("[createVoteUser] 새롭게 투표에 참여하는 유저 테스트")
    public void voteParticipateTest_SUCCESS_newUser(){

        Vote vote = this.voteRepository.findByVoteHash("abcdef");

        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("abcd");
        createVoteUserRequest.setUserImage(1);

        CreateVoteUserResponse voteUser = this.voteService.createVoteUser(createVoteUserRequest, vote.getVoteHash());

        this.entityManager.flush();
        this.entityManager.clear();

        Vote afterParticipate = this.voteRepository.findByVoteHash("abcdef");

        boolean exists = false;

        for (Voter voter : afterParticipate.getVoters()) {
            if (voter.getNickname().equals("abcd") && voter.getProfileImage() == 1) {
                exists = true;
                break;
            }
        }

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("[createVoteUser] 기존 유저가 투표에 참여하는 테스트")
    public void voteParticipateTest_SUCCESS_alreadyExistingUser(){

        Vote vote = this.voteRepository.findByVoteHash("abcdef");

        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("aaa");
        createVoteUserRequest.setUserImage(1);

        CreateVoteUserResponse voteUser = this.voteService.createVoteUser(createVoteUserRequest, vote.getVoteHash());

        this.entityManager.flush();
        this.entityManager.clear();

        int existsCount = 0;

        Vote afterParticipate = this.voteRepository.findByVoteHash("abcdef");

        for (Voter voter : afterParticipate.getVoters()) {
            if (voter.getNickname().equals("aaa") && voter.getProfileImage() == 1) {
                existsCount += 1;
                break;
            }
        }

        Assertions.assertThat(existsCount).isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("[createVoteUser] 투표 참여 실패 (투표 없음)")
    public void voteParticipateTest_FAIL(){

        Vote vote = this.voteRepository.findByVoteHash("abcdef");

        Voter voter1 = Voter.builder().nickname("aaa").profileImage(1).vote(vote).build();

        CreateVoteUserRequest createVoteUserRequest = new CreateVoteUserRequest();
        createVoteUserRequest.setUserName("abcd");
        createVoteUserRequest.setUserImage(1);

        Assertions.assertThatThrownBy(() -> {
            this.voteService.createVoteUser(createVoteUserRequest, "abcde");
        }).isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    @Transactional
    @DisplayName("[createVoteResult] 중복 투표가 아닌데 중복 투표하면 실패")
    public void duplicateVoteFailTest(){

        Vote vote = voteRepository.findByVoteHash("abcdefgh");

        Voter voter = Voter.builder().nickname("abcg").profileImage(1).vote(vote).build();

        this.voterRepository.save(voter);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter.getId())
                .nickname("abcd")
                .options(op)
                .build();

        Assertions.assertThatThrownBy(() -> {

            this.voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("[createVoteResult] 이미 투표했는데 투표 요청 오면 실패")
    public void alreadyVotedFailTest(){

        Vote vote = this.voteRepository.findByVoteHash("abcdef");
        Voter voter = this.voteRepository.findByVoteHash("abcdef").getVoters().get(0);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter.getId())
                .nickname("aaa")
                .options(op)
                .build();

        Assertions.assertThatThrownBy(() -> {
            entityManager.flush();
            entityManager.clear();
            voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("[checkIsExpired] 만료된 투표에 투표하면 실패")
    public void voteTimePassedFailTest(){

        Vote vote = Vote.builder().voteHash("11222").allowDuplicateVote(false).expireAt(LocalDateTime.now().minusHours(1)).build();

        this.voteRepository.save(vote);

        Voter voter = Voter.builder().nickname("abcd").profileImage(1).vote(vote).build();

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter.getId())
                .nickname("abcd")
                .options(op)
                .build();

        Assertions.assertThatThrownBy(() -> {
            entityManager.flush();
            entityManager.clear();
            voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @Transactional
    @DisplayName("[createVoteTest] 정상적으로 투표 참여")
    public void createVoteSuccessTest(){

        Vote vote = this.voteRepository.findByVoteHash("abcdef");
        Voter voter = Voter.builder().vote(vote).nickname("12345").profileImage(2).build();

        this.voterRepository.save(voter);

        List<String> op = new ArrayList<>();
        op.add("13");
        op.add("12");

        CreateVoteResultRequest createVoteResultRequest = CreateVoteResultRequest.builder()
                .userId(voter.getId())
                .nickname(voter.getNickname())
                .options(op)
                .build();

        this.voteService.createVoteResult(vote.getVoteHash() , createVoteResultRequest);

        entityManager.flush();
        entityManager.clear();

        Vote byVoteHash = this.voteRepository.findByVoteHash(vote.getVoteHash());

        int votes = 0;
        for (VoteOption voteOption : byVoteHash.getVoteOptions()) {
            votes += voteOption.getVoteResults().size();
        }

        Assertions.assertThat(votes).isEqualTo(5);
    }
}
