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
        </div>
        <div class="table-cell date-time">
          <div class="reg-date">{{ review.regDate }}</div>
          <div class="like-indicator" :class="{ liked: review.reviewLikeYn }">
            {{ review.reviewLikeYn ? "좋아요" : "싫어요" }}
          </div>
        </div>
        <div class="table-cell activity-status">
          {{ review.reviewBlind ? 'BLIND' : 'ACTIVE' }}
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
        filteredReviews.value = reviews.value; // 기본 전체 조회
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
      currentPage.value = 1; // 검색 후 첫 페이지로 초기화
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
    };
  },
};
</script>

<style scoped>
.review-page {
  width: 80%;
  margin: 20px auto;
  padding: 20px;
  background-color: #f8f8f8;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.search-bar select,
.search-bar input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
}

.search-bar button {
  padding: 8px 12px;
  font-size: 14px;
  color: #fff;
  background-color: #0073e6;
  border-radius: 6px;
}

.review-table {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 3fr 1fr 1fr;
  background-color: #e6f2ff;
  padding: 10px;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 3fr 1fr 1fr;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.review-content p {
  white-space: normal;
  line-height: 1.5;
}

.date-time {
  display: flex;
  flex-direction: column;
}

.like-indicator.liked {
  color: #ff9800;
}

.activity-status {
  font-weight: bold;
}

.activity-status[data-status="ACTIVE"] {
  color: green;
}

.activity-status[data-status="BLIND"] {
  color: red;
}
</style>
