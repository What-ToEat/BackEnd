package capstone.restaurant.dto.vote;

import capstone.restaurant.entity.Voter;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class FindVoteOptionSub {

    private String restaurantId;

    private String restaurantName;

    private String thumbnail;

    private List<FindVoteOptionVoter> voterList;
}
