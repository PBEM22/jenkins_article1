<template>
  <OutfitRecommendationList
      title="게스트 복장 추천 리스트"
      :outfits="outfits"
      :getImageSrc="getImageSrc"
      :showSaveButton="false"
      @itemClicked="alertGuest"
  />
</template>

<script>
import axios from 'axios';
import OutfitRecommendationList from '@/components/outfit/recommend/OutfitRecommendationList.vue';
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';
import { useRouter } from 'vue-router'; // Vue Router 사용을 위해 추가

export default {
  components: { OutfitRecommendationList },
  data() {
    return {
      outfits: null
    };
  },
  async created() {
    await this.fetchOutfitRecommendations();
  },
  setup() {
    const router = useRouter(); // Vue Router 인스턴스 가져오기
    return {router};
  },
  methods: {
    async fetchOutfitRecommendations() {
      const store = useSelectedInfoStore();
      try {
        let date = new Date(store.selectedDate);
        date.setHours(date.getHours() + 9);
        const response = await axios.post('http://localhost:8080/guest/outfit/recommendations', {
          situationSeq: store.selectedSituation,
          requestedAt: date.toISOString().split('.')[0],
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        });
        this.outfits = response.data;
      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }
    },
    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },
    alertGuest() {
      alert("회원 전용 기능입니다. 회원 가입 후 이용해주세요.");
      this.router.push('/login'); // 이름 대신 경로로 이동
    }
  }
};
</script>
