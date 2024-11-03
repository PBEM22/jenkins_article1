package article1be.review.repository;


import article1be.review.aggregate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 유저가 작성한 리뷰 조회
    List<Review> findByUserSeq(Long userSeq);

    // 특정 선택 기록(selectSeq)에 대한 리뷰 조회
    List<Review> findBySelectSeq(Long selectSeq);


}
