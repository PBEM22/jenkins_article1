package article1be.outfit.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SelectOutfitCustomRepository {

    private final EntityManager entityManager;

    public List<Object[]> findStatsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT " +
                "  o.outfit_category AS category, " +
                "  o.outfit_seq AS outfitSeq, " +
                "  o.outfit_name AS outfitName, " +
                "  COUNT(so.select_outfit_seq) AS selectionCount, " +
                "  (COUNT(so.select_outfit_seq) * 100.0 / " +
                "    (SELECT COUNT(so2.select_outfit_seq) " +
                "     FROM select_outfit so2 " +
                "     JOIN outfit o2 ON so2.outfit_seq = o2.outfit_seq " +
                "     WHERE o2.outfit_category = o.outfit_category " +
                "     AND so2.select_seq IN (" +
                "       SELECT sr.select_seq " +
                "       FROM select_record sr " +
                "       WHERE sr.custom_date BETWEEN ?1 AND ?2" +
                "     )" +
                "    )" +
                "  ) AS percentage " +
                "FROM select_outfit so " +
                "JOIN outfit o ON so.outfit_seq = o.outfit_seq " +
                "JOIN select_record sr ON so.select_seq = sr.select_seq " +
                "WHERE sr.custom_date BETWEEN ?1 AND ?2 " +
                "GROUP BY o.outfit_category, o.outfit_seq, o.outfit_name " +
                "ORDER BY o.outfit_category";

        return entityManager.createNativeQuery(query)
                .setParameter(1, startDate) // 첫 번째 위치 파라미터
                .setParameter(2, endDate)   // 두 번째 위치 파라미터
                .getResultList();
    }
}
