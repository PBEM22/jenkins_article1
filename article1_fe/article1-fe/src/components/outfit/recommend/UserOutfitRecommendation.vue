<template>
  <OutfitRecommendationList
      title="회원 복장 추천 리스트"
      :outfits="outfits"
      :isSelected="isSelected"
      :getImageSrc="getImageSrc"
      :showSaveButton="true"
      @itemClicked="toggleSelection"
      @saveSelection="saveSelection"
  />
</template>

<script>
import axios from 'axios';
import OutfitRecommendationList from '@/components/outfit/recommend/OutfitRecommendationList.vue';
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';
import outfitRecommendationResult from "@/views/outfit/recommend/OutfitRecommendationResult.vue";
import { useAuthStore } from "@/store/authStore.js";

export default {
  components: { OutfitRecommendationList },
  data() {
    return {
      outfits: null,
      selectedOutfits: {
        topSeq: null,
        bottomSeq: null,
        shoesSeq: null,
        outerSeq: null,
        accessorySeq: []
      }
    };
  },

  async created() {
    await this.fetchOutfitRecommendations();
  },

  methods: {
    async fetchOutfitRecommendations() {
      const authStore = useAuthStore();
      const store = useSelectedInfoStore();
      try {
        let date = new Date(store.selectedDate);
        date.setHours(date.getHours() + 9);
        console.log(date.toISOString().split('.')[0]);
        const response = await axios.post('/user/outfit/recommendations', {
          situationSeq: store.selectedSituation,
          requestedAt: date.toISOString().split('.')[0],
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authStore.accessToken}`
          }
        });
        this.outfits = response.data;
      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }
    },

    isSelected(category, outfitSeq) {
      if (category === 'ACCESSORY') {
        return this.selectedOutfits.accessorySeq.includes(outfitSeq);
      } else if (category === 'OUTERWEAR') {
        return this.selectedOutfits.outerSeq === outfitSeq;
      } else {
        return this.selectedOutfits[`${category.toLowerCase()}Seq`] === outfitSeq;
      }
    },

    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },

    toggleSelection(category, outfitSeq) {
      if (category === 'ACCESSORY') {
        const index = this.selectedOutfits.accessorySeq.indexOf(outfitSeq);
        if (index >= 0) {
          this.selectedOutfits.accessorySeq.splice(index, 1);
        } else {
          this.selectedOutfits.accessorySeq.push(outfitSeq);
        }
      } else if (category === 'OUTERWEAR') {
        this.selectedOutfits.outerSeq = outfitSeq;
      } else {
        this.selectedOutfits[`${category.toLowerCase()}Seq`] = outfitSeq;
      }
      console.log("Updated selectedOutfits:", this.selectedOutfits);
    },

    async getAddressFromCoordinates(lat, lon) {
      try {
        const token = `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`;

        const response = await axios.get(`https://dapi.kakao.com/v2/local/geo/coord2address.json`, {
          params: {
            x: lon,
            y: lat
          },
          headers: {
            Authorization: token  // Kakao REST API 키를 입력하세요.

          }
        });
        console.log(`KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`);
        const address = response.data.documents[0].address;
        return `${address.region_1depth_name} ${address.region_2depth_name} ${address.region_3depth_name}`;
      } catch (error) {
        console.error("주소 변환에 실패했습니다:", error);
        return `${lat}, ${lon}`; // 변환 실패 시 기본 위도/경도로 반환
      }
    },

    async saveSelection() {
      const store = useSelectedInfoStore();
      const authStore = useAuthStore();
      try {
        let date = new Date(store.selectedDate);
        date.setHours(date.getHours() + 9);

        // 날씨 정보 가져오기
        const weatherResponse = await axios.get('/weather', {
          params: {
            time: date.toISOString().split('.')[0],
            lat: store.selectedLatitude,
            lon: store.selectedLongitude,
          }
        });

        const weatherData = weatherResponse.data;
        const weatherCode = weatherData.nowWeatherCode;
        const highTemp = weatherData.highTemp;
        const lowTemp = weatherData.lowTemp;
        const dailyTemp = highTemp - lowTemp;
        const curTemp = weatherData.nowTemp;
        const precipitation = weatherData.list[0]?.rain?.['1h'] || 0;

        // 위도와 경도를 주소로 변환
        const customLocation = await this.getAddressFromCoordinates(store.selectedLatitude, store.selectedLongitude);

        // 선택한 복장 데이터 전송
        await axios.post('/user/outfit/select', {
          situationSeq: store.selectedSituation,
          customDate: date.toISOString().split('.')[0],
          customLocation: customLocation,  // 변환된 주소 사용
          weatherCode: weatherCode,
          highTemp: highTemp,
          lowTemp: lowTemp,
          dailyTemp: dailyTemp,
          curTemp: curTemp,
          precipitation: precipitation,
          topSeq: this.selectedOutfits.topSeq,
          bottomSeq: this.selectedOutfits.bottomSeq,
          shoesSeq: this.selectedOutfits.shoesSeq,
          outerSeq: this.selectedOutfits.outerSeq,
          accessorySeq: this.selectedOutfits.accessorySeq,
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authStore.accessToken}`
          }
        });
        alert("선택한 복장이 저장되었습니다.");
      } catch (error) {
        console.error("선택한 복장을 저장하지 못했습니다:", error);
        alert("저장 중 오류가 발생했습니다.");
      }
    }
  }
};
</script>
