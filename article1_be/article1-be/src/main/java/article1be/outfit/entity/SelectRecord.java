package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "select_record")
public class SelectRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectSeq;

    private Long userSeq;

    private Long situationSeq;

    private LocalDateTime selectDate;

    private LocalDateTime customDate;

    private String customLocation;

    private Integer weatherCode;

    private Double highTemp;

    private Double lowTemp;

    private Double dailyTemp; //일교차

    private Double curTemp;

    private Double precipitation; //강수량

    public static SelectRecord create(Long userSeq, Long situationSeq, LocalDateTime selectDate,
                                      LocalDateTime customDate,String customLocation, Integer weatherCode,
                                      Double highTemp, Double lowTemp, Double dailyTemp, Double curTemp, Double precipitation) {
        SelectRecord selectRecord = new SelectRecord();
        selectRecord.userSeq = userSeq;
        selectRecord.situationSeq = situationSeq;
        selectRecord.selectDate = selectDate;
        selectRecord.customDate = customDate;
        selectRecord.customLocation = customLocation;
        selectRecord.weatherCode = weatherCode;
        selectRecord.highTemp = highTemp;
        selectRecord.lowTemp = lowTemp;
        selectRecord.dailyTemp = dailyTemp;
        selectRecord.curTemp = curTemp;
        selectRecord.precipitation = precipitation;
        return selectRecord;
    }
}
