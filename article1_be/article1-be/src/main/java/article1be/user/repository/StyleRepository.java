package article1be.user.repository;

import article1be.outfit.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleRepository extends JpaRepository<Style, Long> {

    Style findStyleByStyleSeq(Long styleSeq);

}
