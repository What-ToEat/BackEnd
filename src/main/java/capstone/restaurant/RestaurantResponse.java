package capstone.restaurant;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantResponse extends RestaurantListSub {
    private List<String> reviews;
}
