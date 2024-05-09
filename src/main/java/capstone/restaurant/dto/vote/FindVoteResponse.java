package capstone.restaurant.dto.vote;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FindVoteResponse {

    private String title;

    private String voteHash;

    private LocalDateTime expireAt;

    private Boolean allowDuplicateVote;

    private List<FindVoteOptionSub> voteOptionInfoList;
}
