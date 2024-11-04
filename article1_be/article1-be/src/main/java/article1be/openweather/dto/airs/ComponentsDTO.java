package article1be.openweather.dto.airs;

import lombok.Data;

@Data
public class ComponentsDTO {

    private double co; // 일산화탄소
    private double no; // 질소
    private double no2; // 이산화질소
    private double o3; // 오존
    private double so2; // 이산화황
    private double pm2_5; // 미세먼지(PM2.5)
    private double pm10; // 초미세먼지(PM10)
    private double nh3; // 암모니아
}
