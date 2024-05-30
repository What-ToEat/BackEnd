package capstone.restaurant.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tokens {
    private String accessToken;
    private String refreshToken;
}
