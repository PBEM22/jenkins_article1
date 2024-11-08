<template>
  <div class="map-with-date-view">
    <h2 class="list-title">복장 추천을 받을 위치와 날짜를 입력해주세요</h2>
    <div class="content-container">
      <KakaoMap class="square-component" />
      <DatePicker class="square-component" />
    </div>
    <button class="next-button" @click="goToNextPage">NEXT -></button>
  </div>
</template>

<script>
import KakaoMap from "@/components/outfit/setting/kakaoMap.vue";
import DatePicker from "@/components/outfit/setting/DatePicker.vue";

export default {
  components: {
    KakaoMap,
    DatePicker,
  },
  methods: {
    goToNextPage() {
      this.$router.push({ name: "SelectSituation" });
    },
  },
};
</script>

<style scoped>
.map-with-date-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  height: 100vh;
  background-size: cover;
}

.list-title {
  font-size: 1.5rem;
  color: #333;
  margin-top: 0px; /* 타이틀 상단에 약간의 마진만 추가 */
  margin-bottom: 20px;
}

.content-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 90%;
  max-width: 1000px;
  margin-top: 20px;
  gap: 20px;
}

.square-component {
  width: 100%;
  max-width: 470px; /* 특정 최대 너비로 제한 */
  height: 100%;
  max-height: 470px; /* 특정 최대 높이로 제한 */
}

.next-button {
  margin-top: 20px;
  padding: 10px 30px;
  font-size: 1.1rem;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.next-button:hover {
  background-color: #e0e0e0;
}
</style>
