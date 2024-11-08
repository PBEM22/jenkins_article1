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
      </div>

      <div v-for="(review, index) in paginatedReviews" :key="review.reviewSeq" class="table-row">
        <div class="table-cell">{{ review.userNickname }}</div>
        <div class="table-cell">{{ review.location }}</div>
        <div class="table-cell">{{ review.weather }}°C</div>
        <div class="table-cell review-content">
          <p>{{ review.reviewContent }}</p>
        </div>
        <div class="table-cell date-time">
          <div class="reg-date">{{ review.regDate }}</div>
          <div class="like-indicator">
            {{ review.reviewLikeYn ? "좋아요 👍" : "싫어요 👎" }}
          </div>
          <button class="report-btn" @click="reportReview(review.reviewSeq)">신고</button>
        </div>
      </div>
    </div>

    <!-- Pagination Component -->
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
import { useAuthStore } from '@/store/authStore'; // authStore 경로 확인 필요
import Pagination from '@/components/common/Pagination.vue'; // Pagination 컴포넌트 경로 확인 필요

export default {
  components: {
    Pagination,
  },
  setup() {
    const authStore = useAuthStore();
    const selectedCategory = ref('all');
    const searchQuery = ref('');
    const reviews = ref([]);

    // Pagination state
    const currentPage = ref(1);
    const itemsPerPage = 10;

    const fetchReviews = async () => {
      try {
        const response = await axios.get('/review', {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        reviews.value = response.data;
      } catch (error) {
        console.error("Failed to fetch reviews:", error);
      }
    };

    const filteredReviews = computed(() => {
      return reviews.value.filter((review) => {
        if (selectedCategory.value === 'all') {
          return true;
        } else if (selectedCategory.value === 'author') {
          return review.userNickname.includes(searchQuery.value);
        } else if (selectedCategory.value === 'location') {
          return review.location.includes(searchQuery.value);
        } else if (selectedCategory.value === 'weather') {
          return String(review.weather).includes(searchQuery.value);
        } else if (selectedCategory.value === 'date') {
          return review.regDate.includes(searchQuery.value);
        }
        return false;
      });
    });

    // 페이지별로 표시할 리뷰 계산
    const paginatedReviews = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return filteredReviews.value.slice(start, end);
    });

    // 총 페이지 수 계산
    const totalPages = computed(() => {
      return Math.ceil(filteredReviews.value.length / itemsPerPage);
    });

    const goToPage = (page) => {
      if (page > 0 && page <= totalPages.value) {
        currentPage.value = page;
      }
    };

    const reportReview = (reviewSeq) => {
      console.log(`Review ${reviewSeq} reported.`);
      // 신고 처리 로직을 추가할 수 있습니다.
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
    };
  },
};
</script>

<style scoped>
.review-page {
  width: 80%;
  margin: 20px auto;
  font-family: Arial, sans-serif;
  color: #333;
  background-color: #f8f8f8;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: left;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.search-bar select,
.search-bar input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
}

.search-bar button {
  padding: 8px 12px;
  font-size: 14px;
  cursor: pointer;
  background-color: #0073e6;
  border: none;
  color: white;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.search-bar button:hover {
  background-color: #005bb5;
}

.review-table {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 3fr 1fr;
  padding: 10px;
  background-color: #e6f2ff;
  font-weight: bold;
  color: #333;
  text-align: left;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 3fr 1fr;
  padding: 15px 10px;
  border-bottom: 1px solid #eee;
  align-items: center;
  font-size: 14px;
  color: #444;
}

.table-row:last-child {
  border-bottom: none;
}

.header-cell,
.table-cell {
  padding: 8px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.table-cell.review-content {
  white-space: normal;
  line-height: 1.5;
  color: #555;
}

.table-cell.date-time {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.date-time .reg-date {
  font-size: 12px;
  color: #999;
}

.like-indicator {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.report-btn {
  background-color: transparent;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 12px;
  margin-top: 4px;
  padding: 0;
  text-decoration: underline;
}

.report-btn:hover {
  color: #666;
}
</style>
