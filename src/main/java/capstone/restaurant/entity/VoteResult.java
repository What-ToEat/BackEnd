package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "VOTE_RESULT_SEQ_GEN" , sequenceName = "VOTE_RESULT_SEQUENCE")
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"vote_option_id", "voter_id"})}
)
public class VoteResult {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "VOTE_OPTION_SEQUENCE")
    private Long id;

    @ManyToOne
    @JoinColumn(name="vote_option_id")
    private VoteOption voteOption;

    @ManyToOne
    @JoinColumn(name="voter_id")
    private Voter voter;
}
