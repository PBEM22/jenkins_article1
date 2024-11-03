package article1be.outfit.repository;

import article1be.outfit.entity.OutfitSituation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitSituationRepository extends JpaRepository<OutfitSituation, Long> {
    boolean existsByOutfit_OutfitSeqAndSituation_SituationSeq(Long outfitSeq, Long situationSeq);
}
