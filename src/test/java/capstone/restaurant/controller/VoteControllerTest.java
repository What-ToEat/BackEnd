package capstone.restaurant.controller;

import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.Vote;
import capstone.restaurant.entity.VoteOption;
import capstone.restaurant.entity.Voter;
import capstone.restaurant.repository.RestaurantRepository;
import capstone.restaurant.repository.VoteOptionRepository;
import capstone.restaurant.repository.VoteRepository;
import capstone.restaurant.repository.VoterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VoteControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoterRepository voterRepository;
    @Autowired
    private VoteOptionRepository voteOptionRepository;

    @Test
    @DisplayName("POST /api/votes : 투표 생성 성공")
    void createVoteTest() throws Exception {
        // given
        registerRestaurant(2);

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                .post("/api/vote")
                .contentType("application/json")
                .content("{\"title\": \"동창회 식당 정하기\", \"kakaoId\": \"akssrt163\", \"allowDuplicateVote\": true, \"expiresAt\": \"2024-05-06T13:34:58.987Z\", \"restaurants\": [ \"hash0\", \"hash1\" ]\n}")
                .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/votes : 투표 생성 실패. 등록되지 않은 식당 등록")
    void createVoteFailTest() throws Exception {
        // given
        registerRestaurant(2);

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/vote")
                        .contentType("application/json")
                        .content("{\"title\": \"동창회 식당 정하기\", \"kakaoId\": \"akssrt163\", \"allowDuplicateVote\": true, \"expiresAt\": \"2024-05-06T13:34:58.987Z\", \"restaurants\": [ \"hash0\", \"hash2\" ]\n}")
                        .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isNotFound());
    }

    private void registerRestaurant(int count) {
        for (int i = 0; i < count; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("test" + i)
                    .address("address" + i)
                    .thumbnail("thumbnail" + i)
                    .restaurantHash("hash" + i)
                    .build();
            restaurantRepository.save(restaurant);
        }
    }

    @Test
    @DisplayName("POST /api/votes/{id} : 투표 참여 cookie 테스트")
    public void registerVoteUserTest() throws Exception {

        registerVote();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/vote/123")
                        .contentType("application/json")
                        .content("{\"userName\": \"이광훈\", \"userImage\": 1}")
                        .accept(MediaType.ALL)
        );

        resultActions.andExpect(status().isOk());
    }

    public Vote registerVote(){

        Vote vote = Vote.builder().title("식당 정하기").voteHash("123").expireAt(LocalDateTime.now().plusHours(2L)).voters(new ArrayList<>()).build();
        return voteRepository.save(vote);
    }
    

    @Test
    @DisplayName("POST /api/votes/{id} : 없는 투표 참여 테스트")
    public void registerVoteUserFailTest() throws Exception {
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/vote/123")
                        .contentType("application/json")
                        .content("{\"userName\": \"이광훈\", \"userImage\": 1}")
                        .accept(MediaType.ALL)
        );

        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("DELETE /api/votes/{id}/selection : 없는 투표에 대해 요청시 404")
    void deleteVoteResultFailVoteNotExist() throws Exception {
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/vote/123/selection")
                        .contentType("application/json")
                        .content("{\"nickname\": \"이광훈\", \"userId\": 1}")
                        .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/votes/{id}/selection : 없는 사용자가 요청시 404")
    void deleteVoteResultFailVoterNotExist() throws Exception {
        registerVote();
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/vote/123/selection")
                        .contentType("application/json")
                        .content("{\"nickname\": \"이광훈\", \"userId\": 1}")
                        .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/votes/{id}/selection : 정상적으로 투표 기록 삭제")
    void deleteVoteResultTest() throws Exception {
        Vote vote = registerVote();
        Voter voter = Voter.builder().nickname("qwe").vote(vote).profileImage(1).build();
        voterRepository.save(voter);
        vote.getVoters().add(voter);

        String jsonContent = String.format("{\"nickname\": \"qwe\", \"userId\": %d}" , voter.getId());

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/vote/123/selection")
                        .contentType("application/json")
                        .content(jsonContent)
                        .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/votes/{id}/selection : 정상적으로 투표 수행")
    void postVoteResultTest() throws Exception {

        Restaurant restaurant = Restaurant.builder().name("asdf").restaurantHash("123").build();

        Vote vote = Vote.builder().title("식당 정하기").
                voteHash("123").
                expireAt(LocalDateTime.now().plusHours(2L)).
                allowDuplicateVote(true).
                voters(new ArrayList<>()).build();

        VoteOption voteOption = VoteOption.builder().
                vote(vote).
                restaurant(restaurant).
                build();

        restaurantRepository.save(restaurant);
        voteRepository.save(vote);
        voteOptionRepository.save(voteOption);

        Voter voter = Voter.builder().nickname("qwe").vote(vote).profileImage(1).build();
        voterRepository.save(voter);

        String jsonContent = String.format("{\"nickname\": \"%s\", \"userId\": %d , \"options\": [\"%s\"]}" , "qwe" , voter.getId() , "123");


        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/vote/123/selection")
                        .contentType("application/json")
                        .content(jsonContent)
                        .accept(MediaType.ALL)
        );
        resultActions.andExpect(status().isOk());
    }
}