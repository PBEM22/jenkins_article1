package article1be.admin.repository;

import article1be.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminReviewRepository extends JpaRepository<Review, Long> {


}
