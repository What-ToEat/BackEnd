package capstone.restaurant.repository;

import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.entity.Restaurant;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<RestaurantListSub> findRestaurantListByTag(String place , String[] tags ,  int page);
}
