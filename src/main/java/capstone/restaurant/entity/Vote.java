package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name= "VOTE_SEQ_GEN",
        sequenceName = "VOTE_SEQ")
public class Vote {

    @Id
    @GeneratedValue(generator = "VOTE_SEQ_GEN")
    private Long id;

    @Column(unique = true)
    private String voteHash;

    private String title;

    private LocalDateTime expireAt;

    private String email;

    private Boolean allowDuplicateVote;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<Voter> voters = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteOption> voteOptions = new ArrayList<>();
}
