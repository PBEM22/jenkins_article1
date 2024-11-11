package article1be.outfit.repository;

import article1be.outfit.entity.SelectRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectRecordRepository extends JpaRepository<SelectRecord, Long> {
    List<SelectRecord> findAllByUserSeqOrderBySelectDateDesc(Long userSeq);

}
