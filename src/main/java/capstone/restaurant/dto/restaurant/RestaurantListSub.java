package capstone.restaurant.dto.restaurant;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantListSub {
    private String name;
    private String thumbnail;
    private List<String> tags;
    private String restaurantId;
}
