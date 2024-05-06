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
@SequenceGenerator(name = "VOTE_OPTION_SEQ_GEN" , sequenceName = "VOTE_OPTION_SEQUENCE")
public class VoteOption {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "VOTE_OPTION_SEQUENCE")
    private Long id;

    @ManyToOne
    @JoinColumn(name="vote_id")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;
}
