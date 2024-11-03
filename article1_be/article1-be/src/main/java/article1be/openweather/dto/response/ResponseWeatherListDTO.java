package article1be.openweather.dto.response;

import article1be.common.utils.DateTimeUtil;
import article1be.openweather.dto.weathers.RainDTO;
import article1be.openweather.dto.weathers.WeatherDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseWeatherListDTO {

    // UTC 시간 데이터
    private int dt;
    
    // Main 데이터
    private ResponseMainDTO main;

    // 날씨데이터들
    private List<WeatherDTO> weather;

    // 비 데이터
    private RainDTO rain;

    // LocalDateTime으로 변환한 dt
    private LocalDateTime time;

    // dt를 통해서 LocalDateTime으로 변환 후 저장
    public void setDt(int dt) {
        this.dt = dt;
        this.time = DateTimeUtil.dtParseToLocalDateTime(dt);
    }
}
