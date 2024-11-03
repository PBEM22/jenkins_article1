package article1be.review.repository;

import article1be.common.aggregate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
