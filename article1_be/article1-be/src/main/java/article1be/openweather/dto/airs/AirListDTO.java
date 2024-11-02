package article1be.openweather.dto.airs;

import lombok.Data;

@Data
public class AirListDTO {
    private int dt; // 시간
    private MainAirDTO main; // 주요 정보
    private ComponentsDTO components; // 성분 정보
}
