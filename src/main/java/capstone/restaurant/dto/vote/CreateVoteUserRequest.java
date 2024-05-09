package capstone.restaurant.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateVoteUserRequest {

    @NotNull
    @Schema(example = "이광훈")
    private String userName;

    @NotNull
    @Schema(example = "1" , description = "유저가 사용하는 이미지 번호")
    private Integer userImage;
}
