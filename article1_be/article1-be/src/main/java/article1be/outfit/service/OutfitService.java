package article1be.outfit.service;

import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.service.OpenWeatherService;
import article1be.outfit.dto.OutfitRequestDTO;
import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.OutfitCategory;
import article1be.outfit.entity.OutfitLevel;
import article1be.outfit.repository.OutfitRepository;
import article1be.outfit.repository.OutfitSituationRepository;
import article1be.outfit.repository.OutfitStyleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutfitService {
    private final OpenWeatherService weatherService;
    private final OutfitRepository outfitRepository; // Outfit 데이터를 가져오는 Repository
    private final OutfitSituationRepository outfitSituationRepository;
    private final OutfitStyleRepository outfitStyleRepository;

    public  Map<OutfitCategory, List<OutfitResponseDTO>> getRecommendedOutfits(OutfitRequestDTO requestDTO) throws UnsupportedEncodingException {

        // 1. 날씨 데이터 가져오기 - 날짜,시간이 적용될 수 있도록 수정되어야 할듯.
        ResponseTodayDTO weatherData = weatherService.getTodayWeatherData(String.valueOf(requestDTO.getLatitude()), String.valueOf(requestDTO.getLongitude()));

        if (weatherData == null) {
            throw new IllegalStateException("날씨 정보를 가져올 수 없습니다.");
        }

        // 날씨 데이터에서 필요한 정보 추출
        double maxTemp = weatherData.getHighTemp();
        double minTemp = weatherData.getLowTemp();
        int airQuality = weatherData.getAqi();
        int weatherCode = weatherData.getList().get(0).getWeather().get(0).getId(); // 임시로 첫 번째 날씨 코드 사용
        log.info("최고 온도: "+ maxTemp);
        log.info("최저 온도: "+ minTemp);
        log.info("공기질: "+airQuality);
        log.info("날씨코드: "+weatherCode);

        // 회원의 이전 선택 기록을 가져와서 점수 계산에 활용 - user 구현 후 적용
        /*Set<Long> previouslyChosenOutfitIds = userPreferenceRepository.findPreviouslyChosenOutfitIdsByUser(userSeq);*/

        // 회원의 선호 style - user 구현 후 적용
        // Long styleSeq = preferenceRepository.findStyleSeqByUserSeq(userSeq);

        // test 위한 임시 styleSeq 지정
        long styleSeq = 1;

        // 4. 각 옷에 점수를 매기고 카테고리별로 상위 3개씩 추출
        Map<OutfitCategory, List<OutfitResponseDTO>> recommendedOutfits = new HashMap<>();
        for (OutfitCategory outfitCategory : OutfitCategory.values()) {
            //날씨 정보에 맞는 옷만 필터링
            log.info("카테고리 :"+outfitCategory);
            List<Outfit> outfits = outfitRepository.findByCategoryAndWeatherConditions(outfitCategory,minTemp,maxTemp,weatherCode);
            log.info("outfit들: "+outfits+"\n");
            //필터링된 옷들에 대해 점수를 계산하고 내림차순으로 정렬, 상위 3개만 return.
            List<OutfitResponseDTO> topOutfits = outfits.stream()
                    .sorted((o1, o2) -> Integer.compare(
                            calculateScore(o2, requestDTO.getSituationSeq(), styleSeq, airQuality/*, previouslyChosenOutfitIds*/),
                            calculateScore(o1, requestDTO.getSituationSeq(), styleSeq,airQuality/*, previouslyChosenOutfitIds*/)
                    ))
                    .map(outfit -> new OutfitResponseDTO(outfit.getOutfitSeq(),outfit.getOutfitName(), outfit.getOutfitImg()))
                    .collect(Collectors.toList());

            recommendedOutfits.put(outfitCategory, topOutfits);
        }

        return recommendedOutfits;

    }

    // 각 옷에 점수를 매기는 메서드
    private int calculateScore(Outfit outfit, Long situationSeq , Long styleSeq, int airQuality/*, Set<Long> previouslyChosenOutfitIds*/) {
        int score = 0;

        // 상황에 맞는 옷에 점수 추가
        if (outfitSituationRepository.existsByOutfit_OutfitSeqAndSituation_SituationSeq(outfit.getOutfitSeq(), situationSeq)) {
            score += 10;
        }

        // 회원의 스타일에 맞는 옷에 점수 추가
        if (outfitStyleRepository.existsByOutfit_OutfitSeqAndStyle_StyleSeq(outfit.getOutfitSeq(), styleSeq)) {
            score += 5;
        }

        // 필수, 권장, 선택에 따라 점수 추가
        if (outfit.getOutfitLevel() == OutfitLevel.REQUIRED) {
            score += 20;
        } else if (outfit.getOutfitLevel() == OutfitLevel.RECOMMENDED) {
            score += 10;
        }

        // 해당 outfit이 마스크이고, 공기질이 안좋으면 점수 999점 추가
        if (outfit.getOutfitCategory() == OutfitCategory.ACCESSORY &&
                "마스크".equals(outfit.getOutfitName()) &&
                airQuality >= 4) { // 예를 들어, airQuality가 4 이상이면 공기질이 안 좋음
            score += 999;
        }

        // 회원이 이전에 선택한 옷에 추가 점수
        /*if (previouslyChosenOutfitIds.contains(outfit.getOutfitSeq())) {
            score += 3;
        }*/
        return score;
    }


}
