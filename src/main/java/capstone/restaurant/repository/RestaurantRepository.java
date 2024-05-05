package capstone.restaurant.repository;

import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> , RestaurantRepositoryCustom {

    public Restaurant findRestaurantsByNameContaining(String name);

}
