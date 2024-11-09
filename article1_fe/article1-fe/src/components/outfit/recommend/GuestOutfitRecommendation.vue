<template>
  <div class="recommendation-container">
    <div class="title">{{ title }}</div>
    <div class="content-wrapper">
      <!-- 왼쪽 섹션 -->
      <div class="left-section">
        <div v-for="(items, category) in leftCategories" :key="category" class="category-section">
          <h3>{{ getCategoryName(category) }}</h3>
          <div class="carousel-container">
            <div class="carousel" :style="{ transform: `translateX(-${carouselOffsets[category]}px)` }">
              <div
                  v-for="item in items"
                  :key="item.outfitSeq"
                  class="item-card"
                  @click="alertGuest"
              >
                <img :src="getImageSrc(item.outfitSeq)" alt="Outfit Image" />
                <p>{{ item.outfitName }}</p>
              </div>
            </div>
            <button class="nav-button left" @click="moveCarousel(category, -1)">&#8249;</button>
            <button class="nav-button right" @click="moveCarousel(category, 1)">&#8250;</button>
          </div>
        </div>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section">
        <div v-for="(items, category) in rightCategories" :key="category" class="category-section">
          <h3>{{ getCategoryName(category) }}</h3>
          <div class="carousel-container">
            <div class="carousel" :style="{ transform: `translateX(-${carouselOffsets[category]}px)` }">
              <div
                  v-for="item in items"
                  :key="item.outfitSeq"
                  class="item-card"
                  @click="alertGuest"
              >
                <img :src="getImageSrc(item.outfitSeq)" alt="Outfit Image" />
                <p>{{ item.outfitName }}</p>
              </div>
            </div>
            <button class="nav-button left" @click="moveCarousel(category, -1)">&#8249;</button>
            <button class="nav-button right" @click="moveCarousel(category, 1)">&#8250;</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useSelectedInfoStore } from "@/store/selectedInfoStore.js";
import { useRouter } from "vue-router";

export default {
  data() {
    return {
      outfits: {
        TOP: [],
        BOTTOM: [],
        OUTERWEAR: [],
        SHOES: [],
        ACCESSORY: [],
      },
      carouselOffsets: {
        TOP: 0,
        BOTTOM: 0,
        OUTERWEAR: 0,
        SHOES: 0,
        ACCESSORY: 0,
      },
      itemWidth: 150, // 각 아이템의 너비
      title: "추천 복장 리스트",
    };
  },
  computed: {
    leftCategories() {
      return {
        TOP: this.outfits.TOP,
        BOTTOM: this.outfits.BOTTOM,
        OUTERWEAR: this.outfits.OUTERWEAR,
      };
    },
    rightCategories() {
      return {
        SHOES: this.outfits.SHOES,
        ACCESSORY: this.outfits.ACCESSORY,
      };
    },
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
        const response = await axios.post("http://localhost:8080/guest/outfit/recommendations", {
          situationSeq: store.selectedSituation,
          requestedAt: date.toISOString().split(".")[0],
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        });
        this.outfits = response.data;
      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }
    },
    getCategoryName(category) {
      const categoryNames = {
        TOP: "상의",
        BOTTOM: "하의",
        OUTERWEAR: "아우터",
        SHOES: "신발",
        ACCESSORY: "악세사리",
      };
      return categoryNames[category] || category;
    },
    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },
    alertGuest() {
      alert("로그인이 필요한 서비스입니다.");
      this.router.push("/login");
    },
    moveCarousel(category, direction) {
      const maxOffset = this.outfits[category].length * this.itemWidth - 3 * this.itemWidth;
      const newOffset = this.carouselOffsets[category] + direction * this.itemWidth;
      this.carouselOffsets[category] = Math.min(Math.max(newOffset, 0), maxOffset);
    },
  },
};
</script>

<style scoped>
.recommendation-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0px; /* 패딩 줄이기 */
}

.title {
  font-size: 0.8rem; /* 제목 크기 축소 */
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 1000px; /* 전체 컨테이너 너비 축소 */
}

.left-section,
.right-section {
  display: flex;
  flex-direction: column;
  width: 48%;
}

.category-section {
  margin-bottom: 0px; /* 각 섹션 간격 축소 */
}

.carousel-container {
  position: relative;
  overflow: hidden;
  height: 120px; /* 높이 줄이기 */
  width: calc(90px * 3 + 85px); /* 아이템 3개 + 간격 */
}

.carousel {
  display: flex;
  transition: transform 0.3s ease-in-out;
}

.item-card {
  flex-shrink: 0;
  width: 90px; /* 너비 축소 */
  height: 90px; /* 높이 축소 */
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-right: 40px;
  text-align: center;
  background-color: #f9f9f9;
}

.item-card img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 이미지 비율 유지 */
}

.item-card p {
  margin-top: 5px;
  font-size: 0.8rem; /* 글자 크기 축소 */
}

.item-card.selected {
  border-color: #007bff; /* 선택된 의상에 파란색 테두리 추가 */
  background-color: #e7f0ff; /* 선택된 의상 배경색 변경 */
}
.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  width: 25px;
  height: 25px; /* 버튼 크기 축소 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button.left {
  left: -2px;
}

.nav-button.right {
  right: 5px;
}

.save-button-container {
  margin-top: 70px; /* 선택 완료 버튼 위 간격 축소 */
}

.save-button-container button {
  padding: 5px 10px; /* 버튼 크기 줄이기 */
  font-size: 0.9rem; /* 버튼 텍스트 크기 축소 */
}
</style>
