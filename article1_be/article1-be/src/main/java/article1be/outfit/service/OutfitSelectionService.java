package article1be.outfit.service;

import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.SelectRecord;
import article1be.outfit.entity.SelectOutfit;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OutfitSelectionService {
    private final SelectRecordRepository selectRecordRepository;
    private final SelectOutfitRepository selectOutfitRepository;

    @Transactional
    public void saveSelectedOutfits(OutfitSelectionRequestDTO requestDTO) {
        SelectRecord savedRecord = selectRecordRepository.save(SelectRecord.create(requestDTO));

        // selectSeq 값을 사용하여 SelectOutfit 엔티티 생성 및 저장
        saveSelectedOutfit(savedRecord.getSelectSeq(), requestDTO.getTopSeq());
        saveSelectedOutfit(savedRecord.getSelectSeq(), requestDTO.getBottomSeq());
        saveSelectedOutfit(savedRecord.getSelectSeq(), requestDTO.getShoesSeq());

        if (requestDTO.getOuterSeq() != null) {
            saveSelectedOutfit(savedRecord.getSelectSeq(), requestDTO.getOuterSeq());
        }

        if (requestDTO.getAccessorySeq() != null && !requestDTO.getAccessorySeq().isEmpty()) {
            for (Long accessorySeq : requestDTO.getAccessorySeq()) {
                saveSelectedOutfit(savedRecord.getSelectSeq(), accessorySeq);
            }
        }
    }

    // SelectOutfit 저장
    private void saveSelectedOutfit(Long selectSeq, Long outfitSeq) {
        SelectOutfit selectOutfit = SelectOutfit.create(selectSeq, outfitSeq);
        selectOutfitRepository.save(selectOutfit);
    }
}
