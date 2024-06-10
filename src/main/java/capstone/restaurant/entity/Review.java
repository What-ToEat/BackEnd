package capstone.restaurant.entity;

import capstone.restaurant.dto.restaurant.ReviewListSub;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "REV_SEQ_GEN" , sequenceName = "REVIEW_SEQ")
public class Review {

    @Id
    @GeneratedValue(generator = "REV_SEQ_GEN")
    private Long id;

    private Boolean isAiReview;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static ReviewListSub toDto(Review review) {
        return new ReviewListSub(review.getReview() , review.getIsAiReview());
    }

    public static List<ReviewListSub> toDtoList(List<Review> reviews) {
        return reviews.stream().map(Review::toDto).toList();
    }
}
