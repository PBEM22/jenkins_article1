package article1be.user.repository;

import article1be.user.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Condition, Long> {

    Condition findConditionByConditionSeq(Long conditionSeq);

}
