package article1be.openweather.dto.weathers;

import lombok.Data;

@Data
public class WindDTO {
    private float speed;
    private int deg;
    private float gust;
}
