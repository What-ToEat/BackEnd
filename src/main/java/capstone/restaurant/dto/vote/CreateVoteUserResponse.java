package capstone.restaurant.dto.vote;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateVoteUserResponse {

    private Long userId;

    private Long cookieDuration;
}