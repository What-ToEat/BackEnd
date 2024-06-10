package capstone.restaurant.service;

import capstone.restaurant.dto.vote.*;
import capstone.restaurant.entity.*;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class VoteService {
    private final EmailService emailService;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoterRepository voterRepository;
    private final VoteResultRepository voteResultRepository;

    private static void checkDuplicated(Vote vote, List<String> options) {
        if (!vote.getAllowDuplicateVote() && options.size() >= 2) {
            throw new IllegalArgumentException("중복 투표가 아닌데 2개 이상의 옵션을 골랐습니다.");
        }
    }

    private static void checkUserVoted(Vote vote, Long userId) {
        vote.getVoters().forEach(voter -> {
            if (!voter.getVoteResult().isEmpty() && Objects.equals(voter.getId(), userId)) {
                throw new IllegalArgumentException("이미 투표 했습니다.");
            }
        });
    }

    private static boolean compareByMinute(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.truncatedTo(ChronoUnit.MINUTES).equals(dateTime2.truncatedTo(ChronoUnit.MINUTES));
    }

    @Transactional
    public FindVoteResponse findVote(String voteId) {
        Vote vote = checkVoteExists(voteId);

        return FindVoteResponse.builder()
                .title(vote.getTitle())
                .allowDuplicateVote(vote.getAllowDuplicateVote())
                .expireAt(vote.getExpireAt())
                .voteHash(vote.getVoteHash())
                .voteOptionInfoList(convertVoteEntityToOptionSub(vote))
                .build();
    }

    @Transactional
    public CreateVoteResponse createVote(CreateVoteRequest createVoteRequest) {
        Vote vote = createVoteRequest.toVoteEntity();
        Vote createdVote = voteRepository.save(vote);

        createVoteOption(createVoteRequest.getRestaurants(), createdVote);
        return new CreateVoteResponse(createdVote.getVoteHash());
    }

    private void createVoteOption(List<String> restaurants, Vote vote) {
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

    @Transactional
    public CreateVoteUserResponse createVoteUser(CreateVoteUserRequest createVoteUserRequest, String voteHash) {

        Vote vote = checkVoteExists(voteHash);
        checkIsExpired(vote);

        Voter participatingUser = checkIsParticipatingUser(vote.getVoters(), createVoteUserRequest.getUserName());

        if (participatingUser != null) {
            return new CreateVoteUserResponse(participatingUser.getId(), participatingUser.getNickname(), participatingUser.getProfileImage());
        } else {
            Voter voter = Voter.builder()
                    .nickname(createVoteUserRequest.getUserName())
                    .profileImage(createVoteUserRequest.getUserImage())
                    .vote(vote)
                    .build();

            this.voterRepository.save(voter);

            return new CreateVoteUserResponse(voter.getId(), voter.getNickname(), voter.getProfileImage());
        }
    }

    private Voter checkIsParticipatingUser(List<Voter> voterList, String username) {
        for ( Voter voter : voterList ) {
            if (voter.getNickname().equals(username)) return voter;
        }
        return null;
    }

    public void createVoteResult(String voteHash, CreateVoteResultRequest createVoteResultRequest) {
        Long userId = createVoteResultRequest.getUserId();
        List<String> options = createVoteResultRequest.getOptions();

        Vote vote = checkVoteExists(voteHash);
        checkIsExpired(vote);
        Voter voter = checkVoterExists(voteHash, userId);
        checkUserVoted(vote, userId);
        checkDuplicated(vote, options);

        List<VoteResult> voteResults = new ArrayList<>();
        options.forEach(option -> {
            vote.getVoteOptions().forEach(voteOption -> {
                if (Objects.equals(voteOption.getRestaurant().getRestaurantHash(), option)) {
                    VoteResult voteResult = VoteResult.builder()
                            .voter(voter)
                            .voteOption(voteOption)
                            .build();
                    voteResults.add(voteResult);
                }
            });
        });

        voteResultRepository.saveAll(voteResults);
    }

    @Transactional
    public void deleteVoteResult(String voteHash, DeleteVoteResultRequest deleteVoteResultRequest) {
        Long userId = deleteVoteResultRequest.getUserId();
        Optional<Voter> byId = voterRepository.findById(userId);

        Vote vote = checkVoteExists(voteHash);
        checkIsExpired(vote);
        Voter voter = checkVoterExists(voteHash, userId);

        voteResultRepository.deleteAllByVoter(voter);
    }

    private Voter checkVoterExists(String voteHash, Long userId) {

        Optional<Voter> voter = voterRepository.findById(userId);
        
        if (voter.isEmpty() || !Objects.equals(voter.get().getVote().getVoteHash(), voteHash)) {
            throw new EntityNotFoundException("없는 사용자 입니다.");
        }
        return voter.get();
    }

    private Vote checkVoteExists(String voteHash) {
        Vote vote = this.voteRepository.findByVoteHash(voteHash);
        if (vote == null) {
            throw new EntityNotFoundException("없는 투표 입니다.");
        }
        return vote;
    }

    private List<FindVoteOptionSub> convertVoteEntityToOptionSub(Vote vote) {

        List<FindVoteOptionSub> findVoteOptionSubList = new ArrayList<>();

        for ( VoteOption voteOption : vote.getVoteOptions() ) {
            FindVoteOptionSub findVoteOptionSub = FindVoteOptionSub.builder()
                    .restaurantName(voteOption.getRestaurant().getName())
                    .restaurantId(voteOption.getRestaurant().getRestaurantHash())
                    .thumbnail(voteOption.getRestaurant().getThumbnail())
                    .voterList(getVotersForRestaurant(voteOption.getVoteResults()))
                    .build();

            findVoteOptionSubList.add(findVoteOptionSub);
        }

        return findVoteOptionSubList;
    }

    private List<FindVoteOptionVoter> getVotersForRestaurant(List<VoteResult> voteResultsList) {
        List<FindVoteOptionVoter> voteOptionSubList = new ArrayList<>();

        for ( VoteResult voteResult : voteResultsList ) {
            FindVoteOptionVoter findVoteOptionVoter = FindVoteOptionVoter.builder()
                    .userId(voteResult.getVoter().getId())
                    .userImage(voteResult.getVoter().getProfileImage())
                    .nickname(voteResult.getVoter().getNickname())
                    .build();

            voteOptionSubList.add(findVoteOptionVoter);
        }

        return voteOptionSubList;
    }

    private void checkIsExpired(Vote vote) {
        if (!vote.getExpireAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("투표 기간이 지났습니다.");
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendResult() {
        LocalDateTime now = LocalDateTime.now();
        List<Vote> votes = voteRepository.findAll();
        for ( Vote vote : votes ) {
            if (compareByMinute(now, vote.getExpireAt()) && vote.getEmail() != null) {
                emailService.sendEmail(vote);
            }
        }
    }
}
