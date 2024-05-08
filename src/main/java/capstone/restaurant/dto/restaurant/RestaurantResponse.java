package capstone.restaurant.dto.restaurant;


import capstone.restaurant.dto.tag.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class RestaurantResponse extends RestaurantListSub {

    public RestaurantResponse(String name, String thumbnail, List<TagResponse> tags, String restaurantId, List<ReviewListSub> reviews) {
        super(name, thumbnail, tags, restaurantId);
        this.reviews = reviews;
    }

    @Schema(example = "\"[\\\"맛있어요\\\", \\\"재밌네요\\\"]\"")
    private List<ReviewListSub> reviews;
}
