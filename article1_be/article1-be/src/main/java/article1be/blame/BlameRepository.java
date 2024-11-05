package article1be.blame;

import article1be.blame.entity.Blame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlameRepository extends JpaRepository<Blame, Long> {
}
