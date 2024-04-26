package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Setter
@SequenceGenerator(
        name= "VOTE_SEQ_GEN",
        sequenceName = "VOTE_SEQ")
public class Vote {

    @Id
    @GeneratedValue(generator = "VOTE_SEQ_GEN")
    private Long id;

    private String voteHash;

    private String voteName;
    private LocalDateTime expireAt;
    private String phoneNumber;

    @OneToMany(mappedBy = "vote")
    private List<VoteRestaurant> voteRestaurantList = new ArrayList<VoteRestaurant>();

}
