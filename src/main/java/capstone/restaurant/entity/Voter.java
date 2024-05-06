package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "VOTER_SEQ_GEN" , sequenceName = "VOTER_SEQUENCE")
public class Voter {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "VOTER_SEQUENCE")
    private Long id;

    private String nickname;

    @ManyToOne
    @JoinColumn(name="vote_id")
    private Vote vote;
}
