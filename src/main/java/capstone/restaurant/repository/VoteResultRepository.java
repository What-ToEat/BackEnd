package capstone.restaurant.repository;

import capstone.restaurant.entity.VoteResult;
import capstone.restaurant.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult , Long> {
    void deleteAllByVoter(Voter voter);
}
