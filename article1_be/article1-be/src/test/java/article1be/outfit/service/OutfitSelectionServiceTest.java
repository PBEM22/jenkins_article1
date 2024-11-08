package article1be.outfit.service;

import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.SelectOutfit;
import article1be.outfit.entity.SelectRecord;
import article1be.outfit.repository.OutfitRepository;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OutfitSelectionServiceTest {

    @Mock
    private SelectRecordRepository selectRecordRepository;

    @Mock
    private SelectOutfitRepository selectOutfitRepository;

    @Mock
    private OutfitRepository outfitRepository;

    @InjectMocks
    private OutfitSelectionService outfitSelectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSelectedOutfits_Success() {
        // Mock 데이터 설정
        Long userSeq = 1L;
        Long topSeq = 101L;
        Long bottomSeq = 102L;
        Long shoesSeq = 103L;
        Long outerSeq = 104L;
        Long accessorySeq1 = 201L;
        Long accessorySeq2 = 202L;

        OutfitSelectionRequestDTO requestDTO = new OutfitSelectionRequestDTO(
                1L, LocalDateTime.now(), "Seoul", 800, 25.0, 15.0, 10.0, 20.0, 0.0,
                topSeq, bottomSeq, shoesSeq, outerSeq, Arrays.asList(accessorySeq1, accessorySeq2)
        );

        SelectRecord mockRecord = mock(SelectRecord.class);
        when(selectRecordRepository.save(any(SelectRecord.class))).thenReturn(mockRecord);

        Outfit mockOutfit = mock(Outfit.class);
        when(outfitRepository.findById(anyLong())).thenReturn(Optional.of(mockOutfit));

        // 메서드 실행
        outfitSelectionService.saveSelectedOutfits(requestDTO, userSeq);

        // 호출 횟수 계산 (필수 항목 3개 + 선택 항목 2개 + 악세서리 2개 = 총 7회)
        int expectedCalls = 1 // topSeq
                + 1 // bottomSeq
                + 1 // shoesSeq
                + 1 // outerSeq
                + 2; // accessorySeq(2개)
        verify(selectRecordRepository, times(1)).save(any(SelectRecord.class));
        verify(outfitRepository, times(expectedCalls)).findById(anyLong());
        verify(selectOutfitRepository, times(expectedCalls)).save(any(SelectOutfit.class));
    }


    @Test
    void testSaveSelectedOutfits_InvalidOutfit() {
        // Mock 데이터 설정
        Long userSeq = 1L;
        Long invalidSeq = 999L;

        OutfitSelectionRequestDTO requestDTO = new OutfitSelectionRequestDTO(
                1L, LocalDateTime.now(), "Seoul", 800, 25.0, 15.0, 10.0, 20.0, 0.0,
                invalidSeq, null, null, null, Collections.emptyList()
        );

        SelectRecord mockRecord = mock(SelectRecord.class);
        when(selectRecordRepository.save(any(SelectRecord.class))).thenReturn(mockRecord);

        when(outfitRepository.findById(invalidSeq)).thenReturn(Optional.empty());

        // 메서드 실행 및 예외 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                outfitSelectionService.saveSelectedOutfits(requestDTO, userSeq)
        );

        assertEquals("존재하지 않는 Outfit입니다: 999", exception.getMessage());
        verify(outfitRepository, times(1)).findById(invalidSeq);
    }

    @Test
    void testSaveSelectedOutfits_NoOptionalFields() {
        // Mock 데이터 설정
        Long userSeq = 1L;
        Long topSeq = 101L;
        Long bottomSeq = 102L;
        Long shoesSeq = 103L;

        OutfitSelectionRequestDTO requestDTO = new OutfitSelectionRequestDTO(
                1L, LocalDateTime.now(), "Seoul", 800, 25.0, 15.0, 10.0, 20.0, 0.0,
                topSeq, bottomSeq, shoesSeq, null, null
        );

        SelectRecord mockRecord = mock(SelectRecord.class);
        when(selectRecordRepository.save(any(SelectRecord.class))).thenReturn(mockRecord);

        Outfit mockOutfit = mock(Outfit.class);
        when(outfitRepository.findById(anyLong())).thenReturn(Optional.of(mockOutfit));

        // 메서드 실행
        outfitSelectionService.saveSelectedOutfits(requestDTO, userSeq);

        // 검증
        verify(selectRecordRepository, times(1)).save(any(SelectRecord.class));
        verify(outfitRepository, times(3)).findById(anyLong());
        verify(selectOutfitRepository, times(3)).save(any(SelectOutfit.class));
    }
}
