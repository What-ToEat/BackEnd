package capstone.restaurant.repository;

import capstone.restaurant.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

   Vote findVoteByVoteHash(String voteHash);
}
