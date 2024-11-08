<template>
  <div class="content">
    <!-- 날씨 정보 -->
    <div class="weather-section">
      <WeatherInfo />
    </div>

    <!-- 옷 추천 정보 -->
    <div class="outfit-section">
      <!-- 로그인 상태 확인 -->
      <div v-if="isLoggedIn">
        <UserOutfitRecommendation />
      </div>
      <div v-else>
        <GuestOutfitRecommendation />
      </div>
    </div>
  </div>
</template>

<script setup>
import WeatherInfo from "@/components/WeatherTab.vue";
import UserOutfitRecommendation from "@/components/outfit/recommend/UserOutfitRecommendation.vue";
import GuestOutfitRecommendation from "@/components/outfit/recommend/GuestOutfitRecommendation.vue";
import { useAuthStore } from "@/store/authStore.js";

// 로그인 상태 확인
const authStore = useAuthStore();
const isLoggedIn = authStore.isLoggedIn;

// 디버깅 로그 추가
console.log("현재 로그인 상태:", isLoggedIn ? "로그인됨" : "비로그인 상태");
</script>

<style scoped>
.nav-menu a {
  margin: 0 10px;
  text-decoration: none;
  color: white;
  font-weight: bold;
}

.nav-menu a:hover {
  text-decoration: underline;
}

/* 본문 레이아웃 스타일 */
.content {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: flex-start;
  flex: 1;
  padding: 20px;
  gap: 20px;
}

/* 날씨 정보 섹션 */
.weather-section {
  flex: 1;
  max-width: 400px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
}

/* 옷 추천 정보 섹션 */
.outfit-section {
  flex: 2;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}
</style>
