package article1be.board.repository;

import article1be.board.dto.PictureDTO;
import article1be.board.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findByPictureBoardSeq(long boardSeq);
}
