package capstone.restaurant.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenInfo {
    private Long expiresInMillis;
    private Long id;
    private Long appId;
}
