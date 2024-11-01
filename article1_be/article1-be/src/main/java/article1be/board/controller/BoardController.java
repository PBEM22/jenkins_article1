package article1be.board.controller;

import article1be.amazonS3.service.AmazonS3Service;
import article1be.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "board")
public class BoardController {

    private final AmazonS3Service amazonS3Service;

    @Autowired
    public BoardController(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO>> getBoard() {

        return null;
    }
}
