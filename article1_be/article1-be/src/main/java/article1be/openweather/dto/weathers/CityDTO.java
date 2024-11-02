package article1be.openweather.dto.weathers;

import article1be.openweather.dto.OpenWeather5DayDTO;
import lombok.Data;

@Data
public class CityDTO {
    private int id;
    private String name;
    private CoordDTO coord;
    private String country;
    private int population;
    private int timezone;
    private int sunrise;
    private int sunset;
}
