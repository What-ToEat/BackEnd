package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name= "VOTE_RES_SEQ_GEN",
        sequenceName = "VOTE_RES_SEQ")
@Table(indexes = @Index(columnList = "vote_id"))
public class VoteRestaurant {

    @Id
    @GeneratedValue(generator = "VOTE_RES_SEQ_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String voterName;

    @Column(unique = true)
    private String voterHash;

    private String voterImage;
}
