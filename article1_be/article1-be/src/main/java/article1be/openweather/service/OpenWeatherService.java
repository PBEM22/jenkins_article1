package article1be.openweather.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.common.utils.DateTimeUtil;
import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.response.ResponseAppointDTO;
import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.dto.weathers.WeatherListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class OpenWeatherService {

    // env에 있는 apikey값
    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    // 해당 위치 현재시간 날씨 요청하기 위해서 보내는 주소
    @Value("${open.weather.current.api.url}")
    private String openWeatherApiUrl;

    // 해당 위치 5일간의 날씨데이터 요청하는 주소
    @Value("${open.weather.5day.api.url}")
    private String openWeather5DayApiUrl;

    // 해당 위치 현재시간 대기질을 요청하기 위한 주소
    @Value("${open.weather.air.api.url}")
    private String openWeatherAirApiUrl;

    // 해당 위치 5일간의 대기질을 요청하기 위한 주소
    @Value("${open.weather.air.forecast.url}")
    private String openWeatherAirForecastApiUrl;

    /**
     *  현재 날씨 데이터 불러오기
    * */
    public OpenWeatherDTO getCurrentWeatherData(String lat, String lon) throws UnsupportedEncodingException {

//        urlBuilder.append("?" + URLEncoder.encode("q", "UTF-8") + "=Seoul");
        String urlBuilder = openWeatherApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat +
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey +
                "&" + URLEncoder.encode("lang", "UTF-8") + "=kr" +
                "&" + URLEncoder.encode("units", "UTF-8") + "=metric";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherDTO.class);
    }


    /**
     * 일정시점으로부터 5일동안 3시간 간격의 날씨 데이터 받기
    * */
    public OpenWeather5DayDTO get5DayWeatherData(String lat, String lon, int cnt) throws UnsupportedEncodingException {

        // 가져올 양 = cnt
        String urlBuilder = openWeather5DayApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat +
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("cnt", "UTF-8") + "=" + cnt +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey +
                "&" + URLEncoder.encode("lang", "UTF-8") + "=kr" +
                "&" + URLEncoder.encode("units", "UTF-8") + "=metric";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeather5DayDTO.class);
    }

    /**
     *   현재 시간의 미세먼지 농도를 가져오는 서비스
      */
    public OpenWeatherAirDTO getCurrentAirData(String lat, String lon) throws UnsupportedEncodingException {

        String urlBuilder = openWeatherAirApiUrl + "?" + URLEncoder.encode("lat", "UTF-8") + "=" +  lat+
                "&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon +
                "&" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherApiKey;

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(urlBuilder, OpenWeatherAirDTO.class);
    }

    /**
     * 5일간의 미세먼지 농도를 가져오는 서비스
     */

    // ================== 서비스 로직 ====================== // 
    /**
     *   현재시간에 대한 온도와 체감온도, 미세먼지 농도, 현재 시간~24시까지의 데이터를 반환해주는 서비스 (최고기온, 최저기온, 날씨코드)
      */
    public ResponseTodayDTO getTodayWeatherData(String lat, String lon) throws UnsupportedEncodingException {

        // 위치에 대한 현재 시간에 대한 데이터 가져오기
        OpenWeatherDTO currentWeatherData = getCurrentWeatherData(lat, lon);

        // 현재 시간에 대한 데이터 존재하지 않을 시
        if (currentWeatherData == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 반환 데이터 객체 생성 및 현재 데이터 넣기
        ResponseTodayDTO responseTodayDTO = new ResponseTodayDTO(currentWeatherData);

        // 현재 시간 dt
        long nowDt = currentWeatherData.getDt();

        // dt를 LocalDateTime으로 변환
        LocalDateTime nowTime = DateTimeUtil.dtParseToLocalDateTime(nowDt);

        // 다음날 00시 계산
        LocalDateTime tomorrowTime = getTomorrowTime(nowTime);

        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int cnt = getCnt(nowTime, tomorrowTime);

        // 미래 날씨 데이터 가져오기
        OpenWeather5DayDTO openWeather5DayDTO = get5DayWeatherData(lat, lon, cnt);

        // 데이터가 없으면
        if (openWeather5DayDTO == null){
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 가져온 데이터 넣기
        responseTodayDTO.setList(openWeather5DayDTO.getList());

        // 받은데이터 하루 중 최저 온도, 최고 온도 계산하기
        float minTemp = 100, maxTemp = 0;
        for(WeatherListDTO weatherListDTO : openWeather5DayDTO.getList()){
            // 최저온도
            minTemp = Math.min(minTemp, weatherListDTO.getMain().getTemp_max());

            // 최고온도
            maxTemp = Math.max(maxTemp, weatherListDTO.getMain().getTemp_max());
        }

        // 최저온도 넣기
        responseTodayDTO.setLowTemp(minTemp);

        // 최고온도 넣기
        responseTodayDTO.setHighTemp(maxTemp);

        // 으음.. 만약 앞으로 3시간 뒤는 맑음인데, 6시간 뒤에는 비가 오고, 9시간 뒤에는 맑음이라면??
        // 해당 시간의 미세먼지를 넣기 (대기질 수준 자체를 넣는건 어떨까?)
        OpenWeatherAirDTO currentAirData = getCurrentAirData(lat, lon);

        // 대기질 데이터가 없으면
        if (currentAirData == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_AIR_DATA);
        }
        // 공기질 수준 넣기
        responseTodayDTO.setAqi(currentAirData.getList().get(0).getMain().getAqi());

        return responseTodayDTO;
    }

    // 지정시간 ~ 00시까지의 날씨 데이터 반환 서비스
    public void getAppointmentWeatherData(String inputTime, String lat, String lon) throws UnsupportedEncodingException {

        // 입력 받은 시간을 LocalDateTime으로 변경
        LocalDateTime inputLocalDateTime = DateTimeUtil.stringParseToLocalDateTime(inputTime);

        // 다음날 00시 계산
        LocalDateTime tomorrowTime = getTomorrowTime(inputLocalDateTime);

        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        int cnt = getCnt(inputLocalDateTime, tomorrowTime);

        // 미래 데이터 가져오기
        OpenWeather5DayDTO openWeather5DayDTO = get5DayWeatherData(lat, lon, cnt);

        // 만일 데이터가 없으면 예외 발생
        if (openWeather5DayDTO == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_WEATHER_DATA);
        }

        // 반환할 데이터 DTO 인스턴스 할당
        ResponseAppointDTO responseAppointDTO = new ResponseAppointDTO();
        responseAppointDTO.setList(openWeather5DayDTO.getList());

        // 받은데이터 하루 중 최저 온도, 최고 온도 계산하기
        float minTemp = 100, maxTemp = 0;
        for(WeatherListDTO weatherListDTO : openWeather5DayDTO.getList()){
            // 최저온도
            minTemp = Math.min(minTemp, weatherListDTO.getMain().getTemp_max());

            // 최고온도
            maxTemp = Math.max(maxTemp, weatherListDTO.getMain().getTemp_max());
        }

        // 최저기온
        responseAppointDTO.setLowTemp(minTemp);
        
        // 최고기온
        responseAppointDTO.setHighTemp(maxTemp);


    }

    /**
     * 다음날 00시 구하기
     */
    private LocalDateTime getTomorrowTime(LocalDateTime inputLocalDateTime){
        // 다음날 00시 계산
        return inputLocalDateTime.plusDays(1).toLocalDate().atStartOfDay();
    }

    /**
     * 지정 시간 ~ 다음날 00시 까지의 갯수 구하기
     */
    private int getCnt(LocalDateTime inputTime, LocalDateTime tomorrowTime) {
        // 현재 시간부터 다음날 00시까지 남은 시간 계산하기 (남은 시간 / 3, 다음날 00시 까지의 개수라서 +1)
        long times = Duration.between(inputTime, tomorrowTime).toHours();

        return (int) (times / 3) + 1;
    }

}