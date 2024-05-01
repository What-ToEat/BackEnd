package capstone.restaurant.repository;

import capstone.restaurant.entity.Restaurant;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<Restaurant> findRestaurantListByTagOrKeyWord();
}
