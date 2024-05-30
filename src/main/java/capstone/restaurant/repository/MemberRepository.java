package capstone.restaurant.repository;

import capstone.restaurant.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByProviderId(Long providerId);
}
