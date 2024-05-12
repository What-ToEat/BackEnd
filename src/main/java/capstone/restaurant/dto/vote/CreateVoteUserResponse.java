package capstone.restaurant.dto.vote;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CreateVoteUserResponse {

    private Long userId;

    private String nickname;

    private Integer profileImage;
}
