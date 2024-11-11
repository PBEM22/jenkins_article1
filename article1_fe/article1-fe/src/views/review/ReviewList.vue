<template>
  <div class="review-page">
    <h2>전체 리뷰 조회</h2>
    <div class="search-bar">
      <select v-model="selectedCategory">
        <option value="all">전체</option>
        <option value="author">작성자</option>
        <option value="location">위치</option>
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
          <div class="reg-date">{{ review.regDate.slice(0, 10) }}</div>
          <div class="like-indicator">
            {{ review.reviewLikeYn ? '좋아요 👍' : '싫어요 👎' }}
          </div>
          <button class="report-btn" @click="reportReview(review.reviewSeq)">신고</button>
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
    const reportedReviews = ref([]);
    const currentIndexes = ref([]);

    const currentPage = ref(1);
    const itemsPerPage = 10;

    const fetchReviews = async () => {
      try {
        const response = await axios.get('/review', {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        reviews.value = response.data.sort((a, b) => new Date(b.regDate) - new Date(a.regDate));
        currentIndexes.value = Array(reviews.value.length).fill(0);
      } catch (error) {
        console.error("Failed to fetch reviews:", error);
      }
    };

    const filteredReviews = computed(() => {
      return reviews.value.filter((review) => {
        if (review.delDate !== null) {
          return false;
        }

        if (selectedCategory.value === 'all') {
          return true;
        } else if (selectedCategory.value === 'author') {
          return review.userNickname.includes(searchQuery.value);
        } else if (selectedCategory.value === 'location') {
          return review.location.includes(searchQuery.value);
        }
        return false;
      });
    });

    const paginatedReviews = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return filteredReviews.value.slice(start, end);
    });

    const totalPages = computed(() => {
      return Math.ceil(filteredReviews.value.length / itemsPerPage);
    });

    const goToPage = (page) => {
      if (page > 0 && page <= totalPages.value) {
        currentPage.value = page;
      }
    };

    const reportReview = async (reviewSeq) => {
      if (reportedReviews.value.includes(reviewSeq)) {
        alert('이미 신고된 리뷰입니다.');
        return;
      }

      try {
        await axios.post(`/blame/review/${reviewSeq}`, {}, {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        reportedReviews.value.push(reviewSeq);
        alert('신고가 완료되었습니다.');
      } catch (error) {
        console.error("Failed to report review:", error);
        alert('신고에 실패하였습니다. 다시 시도해 주세요.');
      }
    };

    const getVisibleOutfits = (outfits, reviewIndex) => {
      const startIndex = currentIndexes.value[reviewIndex];
      return outfits.slice(startIndex, startIndex + 2); // 최대 2개의 옷 이미지를 반환
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

    onMounted(fetchReviews);

    return {
      selectedCategory,
      searchQuery,
      reviews,
      filteredReviews,
      paginatedReviews,
      reportReview,
      currentPage,
      totalPages,
      goToPage,
      reportedReviews,
      getVisibleOutfits,
      scrollOutfits
    };
  },
  methods: {
    getImageUrl(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    }
  }
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
  grid-template-columns: 1fr 1fr 1fr 2fr 1fr;
  padding: 10px;
  background-color: #cce4ff;
  border-radius: 8px;
  font-weight: bold;
  text-align: center;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 2fr 1fr;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ddd; /* 일정한 밑줄을 각 행에 적용 */
}

.table-cell {
  padding: 10px 5px; /* 셀마다 균일한 패딩 설정 */
  text-align: center;
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

.reg-date {
  font-size: 16px;
  color: #888;
  font-weight: normal;
}

.like-indicator {
  font-size: 16px;
  color: #555;
  font-weight: bold;
}

.report-btn {
  background-color: transparent;
  border: 1px solid #ddd;
  color: #555;
  cursor: pointer;
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 5px;
}

.outfit-slider {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
  justify-content: center;
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