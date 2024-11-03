package article1be.outfit.service;

import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.SelectRecord;
import article1be.outfit.repository.SelectRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutfitSelectionService {
    private final SelectRecordRepository selectRecordRepository;

    public OutfitSelectionService(SelectRecordRepository selectRecordRepository) {
        this.selectRecordRepository = selectRecordRepository;
    }

    @Transactional
    public void saveSelectedOutfits(OutfitSelectionRequestDTO requestDTO) {
        // SelectRecord 생성 시 customDate 포함
        SelectRecord selectRecord = SelectRecord.create(
                requestDTO.getUserSeq(),
                requestDTO.getSituationSeq(),
                requestDTO.getSelectDate(),
                requestDTO.getCustomDate(),
                requestDTO.getCustomLocation(),
                requestDTO.getWeatherCode(),
                requestDTO.getHighTemp(),
                requestDTO.getLowTemp(),
                requestDTO.getDailyTemp(),
                requestDTO.getCurTemp(),
                requestDTO.getPrecipitation()
        );
        selectRecordRepository.save(selectRecord);
    }
}
