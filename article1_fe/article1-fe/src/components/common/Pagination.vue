<template>
  <div class="pagination">
    <button
        @click="prevPage"
        :disabled="currentPage === 1"
    >
      이전
    </button>

    <span v-for="page in displayedPages" :key="page">
      <button
          @click="goToPage(page)"
          :class="{ active: page === currentPage }"
      >
        {{ page }}
      </button>
    </span>

    <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
    >
      다음
    </button>
  </div>
</template>

<script setup>
import { computed, defineProps } from 'vue';

const props = defineProps({
  currentPage: Number,
  totalPages: Number,
  onPageChange: Function // props에 onPageChange 정의
});

// 페이지 변경 핸들러
const prevPage = () => {
  if (props.currentPage > 1) {
    props.onPageChange(props.currentPage - 1);
  }
};

const nextPage = () => {
  if (props.currentPage < props.totalPages) {
    props.onPageChange(props.currentPage + 1);
  }
};

// 표시할 페이지 번호 계산
const displayedPages = computed(() => {
  const pages = [];
  const total = props.totalPages;
  const current = props.currentPage;

  // 페이지 범위 설정
  let start = Math.max(current - 4, 1); // 현재 페이지 기준으로 왼쪽 4개
  let end = Math.min(current + 5, total); // 현재 페이지 기준으로 오른쪽 5개

  // 페이지 범위가 10개를 초과할 경우 조정
  if (end - start < 9) {
    if (start === 1) {
      end = Math.min(10, total);
    } else if (end === total) {
      start = Math.max(total - 9, 1);
    }
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return pages;
});

// 페이지 이동 핸들러
const goToPage = (page) => {
  if (page !== props.currentPage) {
    props.onPageChange(page);
  }
};
</script>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  gap: 5px; /* 버튼 간격 조정 */
}

button {
  cursor: pointer;
}

button.active {
  font-weight: bold;
  color: black; /* 현재 페이지 강조 */
}

button:disabled {
  color: gray; /* 비활성화된 버튼 색상 */
  cursor: not-allowed;
}
</style>
