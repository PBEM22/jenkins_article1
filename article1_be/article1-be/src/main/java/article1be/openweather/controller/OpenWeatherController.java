package article1be.openweather.controller;

import article1be.openweather.dto.OpenWeather5DayDTO;
import article1be.openweather.dto.OpenWeatherDTO;
import article1be.openweather.dto.OpenWeatherAirDTO;
import article1be.openweather.dto.response.ResponseAppointDTO;
import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    // 위도 경도를 파라미터 값으로 불러오기
    // 현재 데이터 출력
    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {
        OpenWeatherDTO weatherData = openWeatherService.getCurrentWeatherData(lat, lon);

        return ResponseEntity.ok(weatherData);
    }

    // 위도 경도를 파라미터 값으로 불러오기
    // 5일간의 데이터 출력
    @GetMapping("/5day")
    public ResponseEntity<?> get5DayWeather(@RequestParam("lat") String lat,
                                            @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        OpenWeather5DayDTO weatherData = openWeatherService.get5DayWeatherData(lat, lon, 3);

        int dt = weatherData.getList().get(0).getDt();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.of("Asia/Seoul"));

        log.info("가장 앞의 데이터 시간값 가져오기 {}", localDateTime);

        return ResponseEntity.ok(weatherData);
    }

    // 현재시간 공기질 수준을 나타냄
    @GetMapping("/air")
    public ResponseEntity<?> getAirData(@RequestParam("lat") String lat,
                                        @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        OpenWeatherAirDTO currentAirData = openWeatherService.getCurrentAirData(lat, lon);

        return ResponseEntity.ok(currentAirData);
    }

    // 테스트 용도로 만든 현재시간 ~ 다음날 00시 이전의 데이터
    @GetMapping("/today")
    public ResponseEntity<?> getTodayData(@RequestParam("lat") String lat,
                                          @RequestParam("lon") String lon) throws UnsupportedEncodingException {

        ResponseTodayDTO todayWeatherData = openWeatherService.getTodayWeatherData(lat, lon);

        return ResponseEntity.ok(todayWeatherData);
    }

    // 테스트 용도로 만든 지정시간 ~ 다음날 00시 이전의 데이터
    @GetMapping("/appoint")
    public ResponseEntity<?> getAppointData(@RequestParam("time") String inputTime,
                                            @RequestParam("lat") String lat,
                                            @RequestParam("lon") String lon) throws UnsupportedEncodingException{

        ResponseAppointDTO appointmentWeatherData = openWeatherService.getAppointmentWeatherData(inputTime, lat, lon);

        return ResponseEntity.ok(appointmentWeatherData);
    }
}