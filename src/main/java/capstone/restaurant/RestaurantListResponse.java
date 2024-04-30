package capstone.restaurant;

import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
public class RestaurantListResponse {
    private List<RestaurantListSub> restaurants;
}
