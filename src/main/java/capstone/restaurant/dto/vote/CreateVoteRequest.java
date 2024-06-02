package capstone.restaurant.dto.vote;

import capstone.restaurant.entity.Vote;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class CreateVoteRequest {
    @NotNull
    @Schema(example = "동창회 식당 정하기")
    private String title;
    @Schema(example = "akssrt163@naver.com", description = "결과를 이메일로 받고 싶은 경우만 추가")
    private String email;
    @NotNull
    private Boolean allowDuplicateVote;
    @NotNull
    private int expirationTime;
    @NotNull
    @Schema(example = "\"[\\\"qwert\\\", \\\"asdfg\\\"]\"", description = "투표의 옵션으로 넣고 싶은 식당의 restaurantHash 값을 추가한다.")
    private List<String> restaurants;

    public Vote toVoteEntity() {
        String voteHash = UUID.randomUUID().toString().substring(0,8);
        return Vote.builder()
                .title(title)
                .voteHash(voteHash)
                .email(email)
                .allowDuplicateVote(allowDuplicateVote)
                .expireAt(LocalDateTime.now().plusHours(expirationTime))
                .build();
    }
}
