package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@SequenceGenerator(
        name= "VOTE_RES_SEQ_GEN",
        sequenceName = "VOTE_RES_SEQ")
public class VoteRestaurant {

    @Id
    @GeneratedValue(generator = "VOTE_RES_SEQ_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String voterName;

    private String voterHash;

    private Integer voterImage;
}
