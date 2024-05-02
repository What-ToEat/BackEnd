package capstone.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantListParamDto {

    private String place;
    private String tag;
    private Integer page;
    private String searchKeyword;
}
