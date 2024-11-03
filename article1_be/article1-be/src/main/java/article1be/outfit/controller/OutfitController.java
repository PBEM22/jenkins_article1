package article1be.outfit.controller;

import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.dto.OutfitRequestDTO;
import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.dto.OutfitSelectionResponseDTO;
import article1be.outfit.entity.OutfitCategory;
import article1be.outfit.service.OutfitSelectionService;
import article1be.outfit.service.OutfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/user/outfit")
@RequiredArgsConstructor
public class OutfitController {

    private final OutfitService outfitService;
    private final OutfitSelectionService outfitSelectionService;

    @PostMapping("/recommendations")
    public ResponseEntity<Map<OutfitCategory, List<OutfitResponseDTO>>> getTop3OutfitByCategory(@RequestBody OutfitRequestDTO requestDTO) throws IOException {
        Map<OutfitCategory, List<OutfitResponseDTO>> result = outfitService.getRecommendedOutfits(requestDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/select")
    public ResponseEntity<String> selectOutfit(@RequestBody OutfitSelectionRequestDTO requestDTO) {
        outfitSelectionService.saveSelectedOutfits(requestDTO);
        return ResponseEntity.ok("복장이 성공적으로 저장되었습니다.");
    }

}
