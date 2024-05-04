package capstone.restaurant.repository;

import capstone.restaurant.entity.RestaurantTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTagRepository extends JpaRepository<RestaurantTag , Long> {
}
