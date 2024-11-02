package article1be.openweather.dto.airs;

import lombok.Data;

@Data
public class MainAirDTO {
    private int aqi; // 공기질 지수 (1 매우 좋음, 2 좋음, 3 보통, 4 나쁨, 5 매우 나쁨)

}
