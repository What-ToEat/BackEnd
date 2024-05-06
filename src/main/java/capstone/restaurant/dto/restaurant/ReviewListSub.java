package capstone.restaurant.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReviewListSub {

    private String review;

    boolean isAiReview;
}
