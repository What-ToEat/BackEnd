package capstone.restaurant.repository;

import capstone.restaurant.entity.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<TagCategory, Long> {

}
