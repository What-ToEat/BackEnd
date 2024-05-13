package capstone.restaurant.service;

import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.dto.vote.CreateVoteResultRequest;
import capstone.restaurant.dto.vote.DeleteVoteResultRequest;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.Vote;
import capstone.restaurant.entity.Voter;
import capstone.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteOptionRepository voteOptionRepository;

    @Mock
    private VoterRepository voterRepository;

    @Mock
    private VoteResultRepository voteResultRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private VoteService voteService;

    @Test
    @DisplayName("[createVote] 정상적으로 투표 생성")
    void createVoteTest() {
        // Given
        List<String> restaurants = Arrays.asList("qwe123qw", "asd123as");
        CreateVoteRequest createVoteRequest = CreateVoteRequest.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expirationTime(1)
                .restaurants(restaurants)
                .build();
        when(voteRepository.save(any())).thenReturn(createVoteRequest.toVoteEntity());
        when(restaurantRepository.findByRestaurantHash(any())).thenReturn(new Restaurant());
        // When
        voteService.createVote(createVoteRequest);

        // Then
        verify(voteRepository).save(any());
        verify(restaurantRepository, times(2)).findByRestaurantHash(any());
        verify(voteOptionRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("[createVote] 없는 식당을 옵션으로 등록하려는 경우 실패")
    void createVoteFailTest() {
        // Given
        List<String> restaurants = Arrays.asList("qwe123qw", "asd123as");
        CreateVoteRequest createVoteRequest = CreateVoteRequest.builder()
                .title("Test")
                .kakaoId(null)
                .allowDuplicateVote(true)
                .expirationTime(1)
                .restaurants(restaurants)
                .build();
        when(voteRepository.save(any())).thenReturn(createVoteRequest.toVoteEntity());
        when(restaurantRepository.findByRestaurantHash(any())).thenReturn(new Restaurant());
        when(restaurantRepository.findByRestaurantHash("asd123as")).thenReturn(null);

        // When
        assertThrows(Exception.class, () -> voteService.createVote(createVoteRequest));

        // Then
        verify(voteRepository).save(any());
        verify(restaurantRepository, times(2)).findByRestaurantHash(any());
        verify(voteOptionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("[createVoteResult] 투표가 존재 하지 않으면 실패")
    void createVoteResultFailVoteNotExistTest() {
        when(voteRepository.findByVoteHash(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> voteService.createVoteResult("qwe", new CreateVoteResultRequest()));
    }

    @Test
    @DisplayName("[createVoteResult] 투표자 Id가 존재 하지 않으면 실패")
    void createVoteResultFailVoterNotExistTest() {
        Vote vote = Vote.builder()
                .title("test")
                .voteHash("qwe")
                .expireAt(LocalDateTime.now().plusHours(3))
                .build();
        when(voterRepository.findById(any())).thenReturn(Optional.empty());
        when(voteRepository.findByVoteHash(any())).thenReturn(vote);

        assertThrows(EntityNotFoundException.class, () -> voteService.createVoteResult("qwe", new CreateVoteResultRequest()));
    }

    @Test
    @DisplayName("[deleteVoteResult] 투표가 존재 하지 않으면 실패")
    void deleteVoteResultFailVoteNotExistTest() {
        when(voteRepository.findByVoteHash(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> voteService.deleteVoteResult("qwe", new DeleteVoteResultRequest()));
    }

    @Test
    @DisplayName("[deleteVoteResult] 투표자 Id가 존재 하지 않으면 실패")
    void deleteVoteResultFailVoterNotExistTest() {
        Vote vote = Vote.builder()
                .title("test")
                .voteHash("qwe")
                .expireAt(LocalDateTime.now().plusHours(3))
                .build();
        when(voterRepository.findById(any())).thenReturn(Optional.empty());
        when(voteRepository.findByVoteHash(any())).thenReturn(vote);

        assertThrows(EntityNotFoundException.class, () -> voteService.deleteVoteResult("qwe", new DeleteVoteResultRequest()));
    }

    @Test
    @DisplayName("[deleteVoteResult] 투표 기록 정상적으로 삭제")
    void deleteVoteResultTest() {
        Voter voter = Voter.builder()
                .nickname("qwe")
                .id(1L)
                .profileImage(1)
                .vote(Vote.builder().voteHash("qwe").build())
                .build();

        Vote vote = Vote.builder()
                .title("test")
                .voteHash("qwe")
                .expireAt(LocalDateTime.now().plusHours(3))
                .voters(Arrays.asList(voter))
                .build();
        when(voterRepository.findById(any())).thenReturn(Optional.of(voter));
        when(voteRepository.findByVoteHash(any())).thenReturn(vote);

        voteService.deleteVoteResult("qwe",new DeleteVoteResultRequest(1L, "qwe"));

        verify(voteResultRepository).deleteAllByVoter(any());
    }
}