<template>
  <div class="situation-view">
    <h2 class="list-title">그날의 아웃핏 상황을 선택해주세요.</h2>
    <div class="info-box">
      <p><strong>아웃핏 상황 안내</strong></p>
      <ul>
        <li><b>일상:</b> 등교, 출근 등 일상 활동에 어울리는 아웃핏을 추천합니다.</li>
        <li><b>여행:</b> 여행지의 날씨에 맞춰서 활동하기 좋은 아웃핏을 추천합니다.</li>
        <li><b>데이트:</b> 데이트 분위기에 어울리는 세련된 아웃핏을 추천합니다.</li>
        <li><b>운동:</b> 운동이나 야외 활동에 적합한 편안한 아웃핏을 추천합니다.</li>
        <li><b>격식있는자리:</b> 비즈니스 미팅, 발표 등 격식을 차려야 하는 자리에 어울리는 단정한 아웃핏을 추천합니다.</li>
        <li><b>무관:</b> 상황과 관계없이 날씨에 맞는 편안한 아웃핏을 추천합니다.</li>
      </ul>
    </div>
    <div class="situation-container">
      <div
          v-for="situation in situations"
          :key="situation.seq"
          class="situation-option"
          :class="{ selected: selectedSituation === situation.seq }"
          @click="selectSituation(situation.seq)"
      >
        <img :src="situation.icon" :alt="situation.label" />
        <p>{{ situation.label }}</p>
      </div>
    </div>
    <button class="recommend-button" @click="getRecommendation">추천 받기</button>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/authStore.js'; // 인증 상태 확인을 위해 추가

const selectedSituation = ref(null);
const store = useSelectedInfoStore();
const authStore = useAuthStore(); // 인증 상태를 가져오기 위한 Pinia Store
const router = useRouter();

const situations = [
  { label: "일상", seq: 1, icon: new URL('@/assets/images/situation/routine.png', import.meta.url).href },
  { label: "여행", seq: 2, icon: new URL('@/assets/images/situation/travel.png', import.meta.url).href },
  { label: "데이트", seq: 3, icon: new URL('@/assets/images/situation/dating.png', import.meta.url).href },
  { label: "운동", seq: 4, icon: new URL('@/assets/images/situation/sports.png', import.meta.url).href },
  { label: "격식있는자리", seq: 5, icon: new URL('@/assets/images/situation/formal.png', import.meta.url).href },
  { label: "무관", seq: 6, icon: new URL('@/assets/images/situation/none.png', import.meta.url).href },
];

const selectSituation = (seq) => {
  selectedSituation.value = seq;
};

const getRecommendation = () => {
  if (selectedSituation.value) {
    store.selectedSituation = selectedSituation.value; // 상황을 스토어에 저장


    router.push({name: 'weatherAndOutfitResult'});
  } else {
    alert("상황을 선택해 주세요.");
  }
};
</script>

<style scoped>
.situation-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 0px 40px;
  background-size: cover;
  height: 100vh;
  color: #333;
}

.list-title {
  font-size: 1.8rem;
  margin-bottom: 20px;
}

.situation-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  max-width: 800px;
  gap: 20px;
}

.situation-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, background-color 0.2s;
}

.situation-option:hover{
  transform: scale(1.1);
  transition: transform 0.2s ease-in-out;
}

.situation-option.selected {
  background-color: #007bff;
  color: white;
}

.situation-option img {
  width: 80px;
  height: 80px;
  margin-bottom: 8px;
}

.situation-option p {
  font-size: 1rem;
  margin: 0;
}

.recommend-button {
  margin-top: 30px;
  padding: 12px 40px;
  font-size: 1.1rem;
  font-weight: bold;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.recommend-button:hover {
  background-color: #007bff;
}

.info-box {
  padding: 10px;
  max-width: 700px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 15px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
}

.info-box p {
  font-weight: bold;
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 10px;
  text-align: center;
}

.info-box ul {
  text-align: left;
  padding-left: 20px;
  list-style-type: disc;
  font-size: 1rem;
  color: #555;
}

.info-box ul li {
  margin-bottom: 8px;
  line-height: 1.4;
}
</style>
