package capstone.restaurant.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoteResponse {
    @Schema(description = "생성된 투표의 id", example = "qwertyt")
    private String voteHash;
}
