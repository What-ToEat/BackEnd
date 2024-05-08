package capstone.restaurant.repository;

import capstone.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> , RestaurantRepositoryCustom {

    Page<Restaurant> findRestaurantsByNameContaining(String name , Pageable pageable);

    Restaurant findRestaurantByRestaurantHash(String restaurantId);

    Restaurant findByRestaurantHash(String restaurantHash);


}
