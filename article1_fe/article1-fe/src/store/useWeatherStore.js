import { defineStore } from "pinia";
import { ref, computed } from "vue";

// 배경 이미지 변경 store
export const useWeatherStore = defineStore('weather', () => {
    const weatherCode = ref("00"); // 기본 값 설정 (예: 맑은 날씨)

    // 배경 이미지를 날씨 코드에 따라 설정
    const backgroundImage = computed(() => {
        console.log(weatherCode.value);
        switch (weatherCode.value) {
            // 맑음
            case 800:
            case 801:
                return "back-clean.png";

            // 흐림
            case 802:
            case 803:
            case 804:
                return "back-cloud.png";

            // dust 혹은 황사
            case 731:
            case 751:
            case 761:
                return "back-dust.png";

            // 안개
            case 701:
            case 711:
            case 721:
            case 741:
            case 762:
            case 771:
                return "back-fog.png";

            // 비
            case 500:
            case 501:
            case 502:
            case 503:
            case 504:
            case 511:
            case 520:
            case 521:
            case 522:
            case 531:
                return "back-rain.png";

            // 눈
            case 600:
            case 601:
            case 602:
            case 611:
            case 612:
            case 613:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
                return "back-snow.png";

            // 번개
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
            case 781:
                return "back-thunder.png";

            // 필요에 따라 날씨 코드별 이미지 추가
            default:
                return "back-main.png";
        }
    });

    function setWeatherCode(code) {

        weatherCode.value = code;
    }

    return { weatherCode, backgroundImage, setWeatherCode };
});