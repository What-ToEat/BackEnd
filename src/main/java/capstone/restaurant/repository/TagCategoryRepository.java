package capstone.restaurant.repository;

import capstone.restaurant.entity.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagCategoryRepository extends JpaRepository<TagCategory , Long> {
}
