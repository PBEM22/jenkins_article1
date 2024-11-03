package article1be.openweather.dto.response;

import article1be.openweather.dto.weathers.MainDTO;
import lombok.Data;

// 사용자에게 넘겨줄 MainDTO
@Data
public class ResponseMainDTO {
    /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
    private float temp;

    /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
    private float feels_like;

    /** 현재 최저 온도.(대규모 대도시 및 도시 지역 내) */
    private float temp_min;

    /** 현재 최대 온도.(대규모 대도시 및 도시 지역 내)*/
    private float temp_max;

    public ResponseMainDTO(MainDTO mainDTO) {
        this.temp = mainDTO.getTemp();
        this.feels_like = mainDTO.getFeels_like();
        this.temp_min = mainDTO.getTemp_min();
        this.temp_max = mainDTO.getTemp_max();
    }
}
