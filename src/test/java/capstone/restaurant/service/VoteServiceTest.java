package capstone.restaurant.service;

import capstone.restaurant.dto.vote.CreateVoteRequest;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.repository.RestaurantRepository;
import capstone.restaurant.repository.VoteOptionRepository;
import capstone.restaurant.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteOptionRepository voteOptionRepository;
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
}