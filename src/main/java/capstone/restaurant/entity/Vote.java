package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    private String kakaoId;

    private Boolean allowDuplicateVote;

    @OneToMany(mappedBy = "vote")
    private List<Voter> voters = new ArrayList<>();

    @OneToMany(mappedBy = "vote")
    private List<VoteOption> voteOptions = new ArrayList<>();
}
