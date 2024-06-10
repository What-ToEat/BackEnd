package capstone.restaurant.entity;

import capstone.restaurant.dto.restaurant.RestaurantResponse;
import capstone.restaurant.dto.restaurant.ReviewListSub;
import capstone.restaurant.dto.tag.TagResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name= "RESTAURANT_SEQ_GEN",
        sequenceName = "RESTAURANT_SEQ")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTAURANT_SEQ_GEN")
    private Long id;

    @Column(unique = true)
    private String restaurantHash;

    private String address;

    private String thumbnail;

    private String place;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantTag> restaurantTag = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private  List<Review> reviews = new ArrayList<>();

    public static RestaurantResponse toDto(Restaurant restaurant) {
//        List<TagResponse> tagResponseList = new ArrayList<TagResponse>();
//
//        for (RestaurantTag restaurantTag : restaurant.getRestaurantTag()) {
//            tagResponseList.add(new TagResponse(restaurantTag.getTag().getTagName() , restaurantTag.getTag().getTagCategory().getCategoryName()));
//        }

        List<TagResponse> tagResponseList = restaurant.getRestaurantTag()
                .stream().map(restaurantTag -> Tag.toDto(restaurantTag.getTag())).toList();

        List<ReviewListSub> reviewList = Review.toDtoList(restaurant.getReviews());

//        for (Review review : restaurant.getReviews()){
//            reviewList.add(new ReviewListSub(review.getReview() , review.getIsAiReview()));
//        }

        return new RestaurantResponse(restaurant.getName() , restaurant.getThumbnail() , tagResponseList , restaurant.getRestaurantHash() , reviewList);
    }

}
