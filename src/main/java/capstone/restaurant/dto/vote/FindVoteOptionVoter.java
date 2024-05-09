package capstone.restaurant.dto.vote;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class FindVoteOptionVoter {

    private String nickname;

    private Long userId;

    private Integer userImage;
}
