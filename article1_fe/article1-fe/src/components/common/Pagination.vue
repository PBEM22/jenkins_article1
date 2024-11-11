<script setup>
import { computed } from 'vue';
import { defineProps } from 'vue';

const props = defineProps({
  currentPage: Number,
  totalPages: Number,
  goToPage: Function
});

// 페이지 번호 생성
const pages = computed(() => {
  const pageCount = 10; // 표시할 페이지 수
  const total = props.totalPages;

  const pageArray = [];
  const startPage = Math.max(1, props.currentPage - Math.floor(pageCount / 2));
  const endPage = Math.min(total, startPage + pageCount - 1);

  for (let i = startPage; i <= endPage; i++) {
    if (i > 0) {
      pageArray.push(i);
    }
  }

  return pageArray;
});
</script>

<template>
  <div class="pagination">
    <button @click="goToPage(1)" :disabled="currentPage === 1"><<</button>
    <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1"><</button>

    <button v-for="page in pages"
            :key="page"
            @click="goToPage(page)"
            :class="{ active: page === currentPage }">
      {{ page }}
    </button>

    <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">></button>
    <button @click="goToPage(totalPages)" :disabled="currentPage === totalPages">>></button>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin: 20px 0;
  padding: 10px;
  border-radius: 5px;
}

button {
  margin: 0 5px;
  padding: 8px 12px;
  cursor: pointer;
  background: none;
  border: none;
  color: black;
  font-size: 16px;
}

button:disabled {
  cursor: not-allowed;
  color: #ccc;
}

button.active {
  font-weight: bold;
  color: black;
}
</style>