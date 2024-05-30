package capstone.restaurant.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginKakaoRequest {
    @NotNull
    @Schema(example = "kakao Access token")
    private String token;
}