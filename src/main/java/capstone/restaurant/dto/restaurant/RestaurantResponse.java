package capstone.restaurant.dto.restaurant;


import capstone.restaurant.dto.tag.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantResponse extends RestaurantListSub {
    @Schema(example = "\"[\\\"맛있어요\\\", \\\"재밌네요\\\"]\"")
    private List<String> reviews;
}
