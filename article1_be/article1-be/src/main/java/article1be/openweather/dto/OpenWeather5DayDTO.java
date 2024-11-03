package article1be.openweather.dto;

import article1be.openweather.dto.weathers.CityDTO;
import article1be.openweather.dto.weathers.WeatherListDTO;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeather5DayDTO {
    private String cod;
    private int message;
    private int cnt;
    private List<WeatherListDTO> list;
    private CityDTO city;

}