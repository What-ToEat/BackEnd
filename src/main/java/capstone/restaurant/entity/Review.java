package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "REV_SEQ_GEN" , sequenceName = "REVIEW_SEQ")
public class Review {

    @Id
    @GeneratedValue(generator = "REV_SEQ_GEN")
    private Long id;

    private Boolean isAIReview;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
