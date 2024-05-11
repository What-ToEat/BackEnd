package capstone.restaurant.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteVoteResultRequest {
    @NotNull
    @Schema(example = "1")
    private Long userId;

    @NotNull
    @Schema(example = "ghlee12")
    private String nickname;
}
