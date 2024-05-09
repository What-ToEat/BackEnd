package capstone.restaurant.repository;

import capstone.restaurant.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRepository extends JpaRepository<Voter , Long> {
}
