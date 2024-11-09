<template>
  <div class="review-page">
    <h2>전체 리뷰 조회</h2>
    <div class="search-bar">
      <select v-model="selectedCategory">
        <option value="all">전체</option>
        <option value="author">작성자</option>
        <option value="location">위치</option>
        <option value="weather">날씨</option>
        <option value="date">작성일</option>
      </select>
      <input v-model="searchQuery" placeholder="검색" />
      <button @click="searchReviews">검색</button>
    </div>

    <div class="review-table">
      <div class="table-header">
        <span class="header-cell">작성자</span>
        <span class="header-cell">위치</span>
        <span class="header-cell">날씨</span>
        <span class="header-cell">리뷰 내용</span>
        <span class="header-cell">작성일</span>
        <span class="header-cell">활동 상태</span>
      </div>

      <div v-for="(review, index) in paginatedReviews" :key="review.reviewSeq" class="table-row">
        <div class="table-cell">{{ review.userNickname }}</div>
        <div class="table-cell">{{ review.location }}</div>
        <div class="table-cell">{{ review.weather }}°C</div>
        <div class="table-cell review-content">
          <p>{{ review.reviewContent }}</p>

          <!-- 옷 정보 슬라이더 추가 -->
          <div v-if="review.outfits && review.outfits.length > 0" class="outfit-slider">
            <button class="slider-btn" @click="scrollOutfits(index, -1)">←</button>
            <div class="outfit-images">
              <img
                  v-for="outfit in getVisibleOutfits(review.outfits, index)"
                  :key="outfit.outfitSeq"
                  :src="getImageUrl(outfit.outfitSeq)"
                  :alt="outfit.outfitName"
                  class="outfit-image"
              />
            </div>
            <button class="slider-btn" @click="scrollOutfits(index, 1)">→</button>
          </div>
        </div>

        <div class="table-cell date-time">
          <div class="reg-date">{{ review.regDate }}</div>
          <div class="like-indicator">
            {{ review.reviewLikeYn ? '좋아요 👍' : '싫어요 👎' }}
          </div>
        </div>

        <div class="table-cell activity-status">
          <div class="custom-select">
            <select v-model="review.reviewBlind" @change="toggleReviewBlindStatus(review)">
              <option :value="false">ACTIVE</option>
              <option :value="true">BLIND</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :goToPage="goToPage"
    />
  </div>
</template>

<script>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/store/authStore';
import Pagination from '@/components/common/Pagination.vue';

export default {
  components: {
    Pagination,
  },
  setup() {
    const authStore = useAuthStore();
    const selectedCategory = ref('all');
    const searchQuery = ref('');
    const reviews = ref([]);
    const filteredReviews = ref([]);
    const currentIndexes = ref([]);
    const currentPage = ref(1);
    const itemsPerPage = 10;

    const fetchReviews = async () => {
      try {
        const response = await axios.get('/admin/review', {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        reviews.value = response.data;
        filteredReviews.value = reviews.value;
        currentIndexes.value = Array(reviews.value.length).fill(0);
      } catch (error) {
        console.error("Failed to fetch reviews:", error);
      }
    };

    const searchReviews = () => {
      filteredReviews.value = reviews.value.filter((review) => {
        if (selectedCategory.value === 'all') {
          return true;
        } else if (selectedCategory.value === 'author') {
          return review.userNickname?.includes(searchQuery.value);
        } else if (selectedCategory.value === 'location') {
          return review.location?.includes(searchQuery.value);
        } else if (selectedCategory.value === 'weather') {
          return String(review.weather).includes(searchQuery.value);
        } else if (selectedCategory.value === 'date') {
          return review.regDate?.includes(searchQuery.value);
        }
        return false;
      });
      currentPage.value = 1;
    };

    const paginatedReviews = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      return filteredReviews.value.slice(start, start + itemsPerPage);
    });

    const totalPages = computed(() => Math.ceil(filteredReviews.value.length / itemsPerPage));

    const goToPage = (page) => {
      if (page > 0 && page <= totalPages.value) {
        currentPage.value = page;
      }
    };

    const toggleReviewBlindStatus = async (review) => {
      try {
        await axios.put(
            '/admin/review/status',
            {
              reviewSeq: review.reviewSeq,
              reviewBlind: review.reviewBlind,
            },
            {
              headers: {
                Authorization: `Bearer ${authStore.accessToken}`
              }
            }
        );
        alert("상태가 변경되었습니다");
      } catch (error) {
        console.error("Failed to update review blind status:", error);
      }
    };

    const getVisibleOutfits = (outfits, reviewIndex) => {
      const startIndex = currentIndexes.value[reviewIndex];
      return outfits.slice(startIndex, startIndex + 2);
    };

    const scrollOutfits = (reviewIndex, direction) => {
      const maxIndex = Math.max(0, reviews.value[reviewIndex].outfits.length - 2);
      currentIndexes.value[reviewIndex] += direction;
      if (currentIndexes.value[reviewIndex] < 0) {
        currentIndexes.value[reviewIndex] = maxIndex;
      } else if (currentIndexes.value[reviewIndex] > maxIndex) {
        currentIndexes.value[reviewIndex] = 0;
      }
    };

    const getImageUrl = (outfitSeq) => {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    };

    onMounted(fetchReviews);

    return {
      selectedCategory,
      searchQuery,
      filteredReviews,
      paginatedReviews,
      currentPage,
      totalPages,
      goToPage,
      searchReviews,
      toggleReviewBlindStatus,
      getVisibleOutfits,
      scrollOutfits,
      getImageUrl,
    };
  },
};
</script>

<style scoped>
.review-page {
  width: 90%;
  margin: 0 auto;
  font-family: Arial, sans-serif;
  color: #333;
}

h2 {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.search-bar select,
.search-bar input {
  padding: 5px;
  font-size: 14px;
}

.search-bar button {
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
  background-color: #cce4ff;
  border: none;
  color: #333;
}

.review-table {
  background-color: #f9f9ff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.table-header {
  display: grid;
  grid-template-columns: 0.7fr 0.7fr 0.7fr 3fr 1fr 1fr;
  padding: 10px;
  background-color: #cce4ff;
  border-radius: 8px;
  font-weight: bold;
}

.table-row {
  display: grid;
  grid-template-columns: 0.7fr 0.7fr 0.7fr 3fr 1fr 1fr;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.table-cell {
  padding: 10px 5px;
}

.review-content {
  font-size: 16px;
  color: #444;
}

.date-time {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  color: #888;
}

.like-indicator {
  font-size: 16px;
  color: #555;
  font-weight: bold;
}

.custom-select select {
  width: 100%;
  padding: 10px;
  border: none;
  background: none;
  appearance: none;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
}

.outfit-slider {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.outfit-images {
  display: flex;
  overflow-x: hidden;
  gap: 10px;
  max-width: 150px;
}

.outfit-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.slider-btn {
  background-color: #ddd;
  border: none;
  padding: 4px 8px;
  cursor: pointer;
}
</style>
