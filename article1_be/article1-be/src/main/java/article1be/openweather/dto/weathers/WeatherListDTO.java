package article1be.openweather.dto.weathers;

import article1be.openweather.dto.OpenWeather5DayDTO;
import lombok.Data;

import java.util.List;

@Data
public class WeatherListDTO {

    private int dt;
    private MainDTO main;
    private List<WeatherDTO> weather;
    private CloudsDTO clouds;
    private WindDTO wind;
    private int visibility;
    private float pop;
    private RainDTO rain;
    private SysDTO sys;
    private String dt_txt;

}
