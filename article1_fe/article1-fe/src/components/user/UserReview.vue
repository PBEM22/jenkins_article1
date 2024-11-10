<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/authStore.js';
import Pagination from '@/components/common/Pagination.vue';

const authStore = useAuthStore();
const selectedRecords = ref([]);
const myReview = ref([]);
const startDate = ref('');
const endDate = ref('');
const currentPage = ref(1);
const itemsPerPage = 4;

const fetchDataSelectedRecords = async () => {
  try {
    const response = await axios.get("http://localhost:8080/user/selectedRecords", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      selectedRecords.value = response.data;
    } else console.error("아웃핏 이력 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const fetchDataMyReview = async () => {
  try {
    const response = await axios.get("http://localhost:8080/review/myreview", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      myReview.value = response.data;
    } else console.error("리뷰 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const filteredReviews = computed(() => {
  return myReview.value.filter(item => {
    const date = new Date(item.regDate);
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);

    return (!startDate.value || date >= start) && (!endDate.value || date < end);
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredReviews.value.length / itemsPerPage);
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;

  return filteredReviews.value.slice(start, start + itemsPerPage);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;

  currentPage.value = page;
};

onMounted(() => {
  fetchDataSelectedRecords();
  fetchDataMyReview();
});
</script>

<template>
  <div>
    <div class="header">
      <h1>내가 작성한 리뷰</h1>
      <div class="search-period">
        <label for="startDate">검색 기간</label>
        <input type="date" id="startDate" v-model="startDate" />
        <span>~</span>
        <input type="date" id="endDate" v-model="endDate"/>
      </div>
    </div>

    <div class="review-list">
      <div class="review-item" v-for="item in paginatedReviews" :key="item.reviewSeq">
        <div class="review-details">
          <div>selectDate : {{ selectedRecords.find(user => user.selectSeq === item.selectSeq)?.selectDate.slice(0, 10) }}</div>
          <div>customDate : {{ selectedRecords.find(user => user.selectSeq === item.selectSeq)?.customDate.slice(0, 10) }}</div>
          <div>위치 : {{ item.location }}</div>
          <div>날씨 : {{ item.weather }}°C</div>
          <div>리뷰 내용 : {{ item.reviewContent }}</div>
          <div>작성일 : {{ item.regDate.slice(0, 10) }}</div>
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

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.search-period {
  display: flex;
  align-items: center;
}

.search-period label {
  margin-right: 20px;
  margin-left: 10px;
  font-size: 16px;
  font-weight: bold;
}

.review-list {
  display: flex;
  flex-direction: column;
}

.review-item {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border: 1px solid #ddd;
  margin-bottom: 1rem;
  border-radius: 5px;
  background-color: #f9f9f9;
}
</style>
