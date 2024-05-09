package capstone.restaurant.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVoteResultRequest {
    @NotNull
    @Schema(example = "1")
    private int userId;

    @NotNull
    @Schema(example = "ghlee12")
    private String nickname;

    @NotNull
    @Schema(description = "투표 하려는 식당의 아이디", example = "\"[\\\"qwe\\\", \\\"asd\\\"]\"")
    private List<String> options;
}
