package capstone.restaurant.dto.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantListSub {
    @Schema(example = "롯데리아")
    private String name;
    @Schema(example = "http://www.sdffff.bgrr/123t653")
    private String thumbnail;
    @Schema(example = "\"[\\\"맛있는\\\", \\\"깨끗한\\\"]\"")
    private List<String> tags;
    @Schema(example = "qwe1245")
    private String restaurantId;
}
