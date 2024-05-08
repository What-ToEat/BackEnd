package capstone.restaurant.controller;

import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.repository.RestaurantRepository;
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

import static org.junit.jupiter.api.Assertions.*;
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
}