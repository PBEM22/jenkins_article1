package article1be.openweather.dto;

import article1be.openweather.dto.weathers.RainDTO;
import article1be.openweather.dto.weathers.SnowDTO;
import article1be.openweather.dto.weathers.WeatherListDTO;
import lombok.Data;

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
    private List<WeatherListDTO> list;

    // 하루 중 최저온도
    private float lowTemp;

    // 하루 중 최고온도
    private float highTemp;

    // 공기질 지수 (1 매우 좋음, 2 좋음, 3 보통, 4 나쁨, 5 매우 나쁨)
    private int aqi;

//    // 메인 날씨 데이터
//    @Data
//    public static class Main{
//        // 해당시간 온도
//        private float temp;
//
//        // 해당시간 체감 온도
//        private float feelsLike;
//
//        // 해당시간 최저온도
//        private float tempMin;
//
//        // 해당시간 최고온도
//        private float tempMax;
//    }

    // 눈이 왔을 때의 변수 클래스
//    @Data
//    public static class Snow{
//        // 지난 1시간동안의 눈량
//        private float snow1h;
//
//        // 지난 3시간동안의 눈량
//        private float snow3h;
//    }

//    // 각 시간별 데이터들
//    @Data
//    public static class WeatherList{
//        // UTC로의 시간 값
//        private int dt;
//
//        // 해당 시간 온도 데이터
//        private Main main;
//
//        // 해당 시간 날씨 데이터
//        private List<Weather> weather;
//
//        // 해당 시간 비 데이터
//        private OpenWeatherDTO.Rain rain;
//    }

    // 날씨 데이터
//    @Data
//    public static class Weather{
//        // 해당시간 날씨 코드
//        private int code;
//
//        // 해당 시간 날씨
//        private String weatherStr;
//
//        // 해당 시간 날씨 설명
//        private String weatherDescription;
//
//        // 아이콘
//        private String icon;
//    }

    // 현재 날씨 데이터 넣기
    public ResponseTodayDTO(OpenWeatherDTO openWeatherDTO) {
        this.nowWeatherCode = openWeatherDTO.getWeather().get(0).getId();
        this.nowTemp = openWeatherDTO.getMain().getTemp();
        this.nowFeelsLike = openWeatherDTO.getMain().getFeels_like();
        this.nowRain = openWeatherDTO.getRain();
    }
}
