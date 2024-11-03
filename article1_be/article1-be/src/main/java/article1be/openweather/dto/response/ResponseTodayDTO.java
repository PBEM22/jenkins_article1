package article1be.openweather.dto.response;

import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.weathers.RainDTO;
import article1be.openweather.dto.weathers.SnowDTO;
import article1be.openweather.dto.weathers.WeatherListDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseTodayDTO {

    // 현재날씨코드
    private int nowWeatherCode;

    // 현재 온도
    private float nowTemp;

    // 현재 체감 온도
    private float nowFeelsLike;

    // 코드가 비일때 들어오는 데이터
    private RainDTO nowRain;

    // 코드가 눈일때 들어오는 데이터
    private SnowDTO snow;

    // 지정시점 ~ 24시이전까지의 날씨 데이터
    private List<ResponseWeatherListDTO> list;

    // 하루 중 최저온도
    private float lowTemp;

    // 하루 중 최고온도
    private float highTemp;

    // 공기질 지수 (1 매우 좋음, 2 좋음, 3 보통, 4 나쁨, 5 매우 나쁨)
    private int aqi;

    // 현재 날씨 데이터 넣기
    public ResponseTodayDTO(OpenWeatherDTO openWeatherDTO) {
        this.nowWeatherCode = openWeatherDTO.getWeather().get(0).getId();
        this.nowTemp = openWeatherDTO.getMain().getTemp();
        this.nowFeelsLike = openWeatherDTO.getMain().getFeels_like();
        this.nowRain = openWeatherDTO.getRain();
    }

    // WeatherListDTO 변환 및 세팅
    public void setList(List<WeatherListDTO> list) {
        // 변환한 데이터를 담을 리스트 생성
        List<ResponseWeatherListDTO> responseList = new ArrayList<>();

        // 데이터 변환
        for (WeatherListDTO weatherListDTO : list) {

            // DTO 데이터 변환
            ResponseWeatherListDTO responseWeatherListDTO = new ResponseWeatherListDTO();
            responseWeatherListDTO.setDt(weatherListDTO.getDt());
            responseWeatherListDTO.setWeather(weatherListDTO.getWeather());

            // MainDTO 데이터 변환
            ResponseMainDTO responseMainDTO = new ResponseMainDTO(weatherListDTO.getMain());
            // MainDTO 저장
            responseWeatherListDTO.setMain(responseMainDTO);
            // Rain 데이터 저장
            responseWeatherListDTO.setRain(weatherListDTO.getRain());

            // 저장
            responseList.add(responseWeatherListDTO);
        }

        // 저장
        this.list = responseList;
    }
}
