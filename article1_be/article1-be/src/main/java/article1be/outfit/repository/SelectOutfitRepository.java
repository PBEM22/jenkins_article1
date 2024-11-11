package article1be.outfit.repository;

import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.SelectOutfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelectOutfitRepository extends JpaRepository<SelectOutfit, Long> {

    // 특정 Outfit과 UserSeq를 기준으로 선택 횟수 조회
    int countByOutfitAndSelectRecord_UserSeq(Outfit outfit, Long userSeq);
    List<SelectOutfit> findBySelectRecord_SelectSeq(Long selectSeq);

    @Query("SELECT so FROM SelectOutfit so JOIN FETCH so.outfit WHERE so.selectRecord.selectSeq = :selectSeq")
    List<SelectOutfit> findBySelectRecord_SelectSeqWithOutfit(@Param("selectSeq") Long selectSeq);
}
