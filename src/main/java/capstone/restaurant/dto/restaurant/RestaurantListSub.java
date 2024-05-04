package capstone.restaurant.dto.restaurant;

import capstone.restaurant.dto.tag.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RestaurantListSub {
    @Schema(example = "롯데리아")
    private String name;
    @Schema(example = "http://www.sdffff.bgrr/123t653")
    private String thumbnail;
    private List<TagResponse> tags;
    @Schema(example = "qwe1245")
    private String restaurantId;


}
