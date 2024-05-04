package capstone.restaurant.repository;

import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.entity.Restaurant;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<RestaurantListSub> findRestaurantListByTagOrKeyWord(String place , String[] tags , String search , int page);
}
