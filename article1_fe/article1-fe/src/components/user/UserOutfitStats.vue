<template>
  <div class="outfit-stats">
    <!-- 계절 선택 -->
    <div class="season-selector">
      <button
          v-for="season in seasons"
          :key="season"
          :class="{ active: selectedSeason === season }"
          :style="selectedSeason === season ? { backgroundColor: getSeasonColor(season) } : {}"
          @click="selectSeason(season)"
      >
        {{ season }}
      </button>
    </div>

    <!-- 카테고리별 통계 -->
    <div v-for="category in sortedCategories" :key="category.category" class="category">
      <!-- 카테고리 제목 -->
      <div class="category-header">
        <img :src="getCategoryIcon(category.category)" alt="Category Icon" />
        <span>{{ getCategoryDisplayName(category.category) }}</span>
      </div>

      <!-- 아웃핏 통계 리스트 -->
      <div class="outfit-list">
        <div
            v-for="outfit in category.outfits"
            :key="outfit.outfitSeq"
            class="outfit-item"
        >
          <!-- 아웃핏 이름과 통계 -->
          <div class="outfit-info">
            <span class="outfit-name">{{ outfit.outfitName }}</span>
            <span class="outfit-percentage">
              {{ outfit.selectionCount }}번 ({{ outfit.percentage.toFixed(2) }}%)
            </span>
          </div>

          <!-- 바 그래프 -->
          <div class="progress-bar">
            <div
                class="progress-fill"
                :style="{ width: outfit.percentage + '%' }"
            ></div>
          </div>

          <!-- 아웃핏 이미지 -->
          <img
              :src="getOutfitImage(outfit.outfitSeq)"
              alt="Outfit Image"
              class="outfit-image"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";
import { useAuthStore } from "@/store/authStore.js"; // Pinia AuthStore 가져오기

// AuthStore 사용
const authStore = useAuthStore();

// 상태 관리
const categories = ref([]);
const selectedSeason = ref("봄");
const seasons = ["봄", "여름", "가을", "겨울"];

// 계절 색상 매핑
const seasonColors = {
  봄: "#FFD700", // 노랑
  여름: "#FF4500", // 빨강
  가을: "#8B4513", // 갈색
  겨울: "#1E90FF", // 파랑
};

// 계절 색상 가져오기
const getSeasonColor = (season) => seasonColors[season] || "#f0f0f0";

// 카테고리 우선순위
const categoryOrder = ["TOP", "BOTTOM", "OUTERWEAR", "SHOES", "ACCESSORY"];

// 카테고리 정렬된 목록 계산
const sortedCategories = computed(() => {
  return categories.value.sort((a, b) => {
    const indexA = categoryOrder.indexOf(a.category);
    const indexB = categoryOrder.indexOf(b.category);
    return indexA - indexB; // 카테고리 우선순위로 정렬
  });
});

// 카테고리 한글 이름 매핑
const getCategoryDisplayName = (category) => {
  const categoryNames = {
    TOP: "상의",
    BOTTOM: "하의",
    OUTERWEAR: "아우터",
    SHOES: "신발",
    ACCESSORY: "악세서리",
  };
  return categoryNames[category] || category; // 매핑되지 않은 경우 기본값으로 원래 이름 반환
};

// 계절별 데이터 API 호출
const fetchSeasonData = async (season) => {
  try {
    // 계절별 기간 매핑
    const seasonMapping = {
      봄: { startDate: "2024-03-01", endDate: "2024-05-31" },
      여름: { startDate: "2024-06-01", endDate: "2024-08-31" },
      가을: { startDate: "2024-09-01", endDate: "2024-11-30" },
      겨울: { startDate: "2024-12-01", endDate: "2024-02-28" },
    };
    const { startDate, endDate } = seasonMapping[season];

    // API 호출
    const response = await axios.get("/user/outfit/stats", {
      params: { startDate, endDate },
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authStore.accessToken}`, // AuthStore의 AccessToken 사용
      },
    });

    // API 응답 데이터 반영
    categories.value = response.data.categories.map((category) => ({
      ...category,
      outfits: category.outfits.sort((a, b) => b.selectionCount - a.selectionCount), // 선택 횟수로 내림차순 정렬
    }));
  } catch (error) {
    console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
  }
};

// 초기 데이터 및 계절 변경 시 호출
const selectSeason = (season) => {
  selectedSeason.value = season;
  fetchSeasonData(season);
};

// 컴포넌트가 마운트되면 초기 데이터를 가져옵니다.
onMounted(() => {
  if (authStore.isLoggedIn) {
    fetchSeasonData(selectedSeason.value);
  } else {
    console.warn("로그인이 필요합니다.");
  }
});

// 이미지 경로 생성
const getOutfitImage = (outfitSeq) => {
  return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
};

// 카테고리 아이콘 경로 생성
const getCategoryIcon = (category) => {
  const icons = {
    TOP: new URL("/src/assets/images/outfits/icons/top.png", import.meta.url).href,
    BOTTOM: new URL("/src/assets/images/outfits/icons/bottom.png", import.meta.url).href,
    OUTERWEAR: new URL("/src/assets/images/outfits/icons/outerwear.png", import.meta.url).href,
    SHOES: new URL("/src/assets/images/outfits/icons/shoes.png", import.meta.url).href,
    ACCESSORY: new URL("/src/assets/images/outfits/icons/accessory.png", import.meta.url).href,
  };
  return icons[category] || new URL("/src/assets/images/icons/default.png", import.meta.url).href;
};
</script>

<style scoped>
.outfit-stats {
  font-family: Arial, sans-serif;
  padding: 20px;
  background-color: #f5f5f5;
}

.season-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.season-selector button {
  margin: 0 10px;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  font-size: 16px;
  border-radius: 5px;
  color: #fff;
}

.season-selector button.active {
  font-weight: bold;
}

.category {
  margin-bottom: 40px;
}

.category-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.category-header img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.category-header span {
  font-size: 24px;
  font-weight: bold;
}

.outfit-list {
  margin-left: 20px;
}

.outfit-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.outfit-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  margin-right: 10px;
}

.outfit-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.progress-bar {
  flex: 1;
  background-color: #e0e0e0;
  height: 10px;
  position: relative;
  border-radius: 5px;
}

.progress-fill {
  background-color: #42a5f5;
  height: 100%;
  border-radius: 5px;
}

.outfit-image {
  width: 50px;
  height: 50px;
  border-radius: 5px;
  object-fit: cover;
  margin-left: 10px;
}
</style>
