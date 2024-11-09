<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/authStore.js';
import Pagination from '@/components/common/Pagination.vue';

const authStore = useAuthStore();
const startDate = ref('');
const endDate = ref('');
const selectedRecords = ref([]);
const selectedOutfit = ref([]);
const currentPage = ref(1);
const itemsPerPage = 4;
const showModal = ref(false);
const selectedOutfitId = ref(null);
const reviewText = ref('');
const feedback = ref('');

const fetchDataSelectedRecords = async () => {
  try {
    const response = await axios.get("http://localhost:8080/user/selectedRecords", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      selectedRecords.value = response.data;

      // 모든 선택된 기록에 대해 아웃핏 데이터 조회
      for (const record of selectedRecords.value) {
        await fetchDataSelectedOutfit(record.selectSeq);
      }
    } else console.error("아웃핏 이력 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const fetchDataSelectedOutfit = async (selectSeq) => {
  try {
    const response = await axios.get("http://localhost:8080/user/selectedOutfit", {
      params: { selectSeq: selectSeq },
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      selectedOutfit.value.push(response.data);

      console.log(selectedOutfit.value);
    } else console.error("아웃핏 이력 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("아웃핏 정보를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const filteredSelectedRecords = computed(() => {
  return selectedRecords.value.filter(item => {
    const date = new Date(item.selectDate);
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);

    return (!startDate.value || date >= start) && (!endDate.value || date < end);
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredSelectedRecords.value.length / itemsPerPage);
});

const paginatedSelectedRecords = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;

  return filteredSelectedRecords.value.slice(start, start + itemsPerPage);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;

  currentPage.value = page;
};

const openModal = (id) => {
  selectedOutfitId.value = id;
  showModal.value = true;
};

const submitReview = () => {
  alert(`리뷰가 제출되었습니다: ${reviewText.value} (의견: ${feedback.value})`);

  reviewText.value = '';
  feedback.value = '';
  showModal.value = false;
};

onMounted(() => {
  fetchDataSelectedRecords();
});
</script>

<template>
  <div>
    <div class="header">
      <h1>아웃핏 이력 조회</h1>
      <div class="search-period">
        <label for="startDate">검색 기간</label>
        <input type="date" id="startDate" v-model="startDate" />
        <span>~</span>
        <input type="date" id="endDate" v-model="endDate" />
      </div>
    </div>

    <div class="outfit-list">
      <div class="outfit-item" v-for="item in paginatedSelectedRecords" :key="item.selectSeq">
        <div class="outfit-details">
          <div>selectDate : {{ item.selectDate.slice(0, 10) }}</div>
          <div>customDate : {{ item.customDate.slice(0, 10) }}</div>
          <div>위치 : {{ item.customLocation }}</div>
          <div>날씨 : {{ item.curTemp }}°C</div>
          <div>상의 : {{ selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.topName.replace(/_/g, ' ') || '정보 없음' }}</div>
          <div>하의 : {{ selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.bottomName.replace(/_/g, ' ') || '정보 없음' }}</div>
          <div>신발 : {{ selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.shoesName.replace(/_/g, ' ') || '정보 없음' }}</div>
          <div>아우터 : {{ selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.outerName.replace(/_/g, ' ') || '정보 없음' }}</div>
          <div>악세서리 : {{ selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.accessoryNames.join(', ').replace(/_/g, ' ') || '정보 없음' }}</div>
        </div>
        <button class="review-button" @click="openModal(item.selectSeq)">리뷰 작성하기</button>
      </div>
    </div>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :goToPage="goToPage"
    />

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2 class="modal-title">리뷰 작성</h2>
          <button class="close-button" @click="showModal = false">X</button>
        </div>
        <div class="modal-body">
          <div class="feedback-options">
            <label class="feedback-label">
              <input type="radio" value="좋아요" v-model="feedback" />
              <span class="circle" :class="{ selected: feedback === '좋아요' }"></span>
              <span class="text" style="margin-left: 10px; margin-right: 10px;">좋아요</span>
              <span class="icon">👍</span>
            </label>
            <label class="feedback-label">
              <input type="radio" value="싫어요" v-model="feedback" />
              <span class="circle" :class="{ selected: feedback === '싫어요' }"></span>
              <span class="text" style="margin-left: 10px; margin-right: 10px;">싫어요</span>
              <span class="icon">👎</span>
            </label>
          </div>
          <textarea v-model="reviewText" placeholder="한 줄 리뷰를 작성하여 의견을 말씀해 주세요." class="review-textarea"></textarea>
        </div>
        <div class="modal-footer">
          <button class="review-submit" @click="submitReview">리뷰쓰기</button>
        </div>
      </div>
    </div>
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

.outfit-list {
  display: flex;
  flex-direction: column;
}

.outfit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #ddd;
  margin-bottom: 1rem;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.review-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.review-button:hover {
  background-color: #0056b3;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 800px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.modal-title {
  flex-grow: 1;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.feedback-options {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.feedback-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 0.5rem;
}

.feedback-options input[type="radio"] {
  display: none;
}

.circle {
  border: 2px solid #007BFF;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  position: relative;
  transition: background-color 0.3s, border-color 0.3s;
}

.circle.selected {
  background-color: #007BFF;
  border: 2px solid #007BFF;
}

.icon {
  font-size: 30px;
}

.review-textarea {
  width: 100%;
  height: 80px;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: none;
  margin-bottom: 1rem;
}

.review-submit {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
}

.review-submit:hover {
  background-color: #0056b3;
}
</style>