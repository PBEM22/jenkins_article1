<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  seq: Number,
  type: String,
  content: String,
  writer: Number,
  status: String // status는 문자열로 받아옴
});

const emit = defineEmits(['update:status']); // 이벤트 정의
</script>

<template>
  <div class="review-card">
    <div class="item">{{ seq }}</div>
    <div class="item">{{ type }}</div>
    <div class="item">{{ content }}</div>
    <div class="item">{{ writer }}</div>
    <div class="item">
      <select :value="status" @change="event => emit('update:status', event.target.value)">
        <option value="true">블라인드</option>
        <option value="false">해제</option>
      </select>
    </div>
  </div>
</template>

<style scoped>
.review-card {
  display: flex; /* Flexbox 사용 */
  align-items: center; /* 세로 중앙 정렬 */
  border: 1px solid #ccc; /* 테두리 추가 */
  background-color: #f9f9f9; /* 배경색 추가 */
  padding: 8px; /* 패딩 추가 */
  margin: 8px 0; /* 상하 여백 추가 */
}

.item {
  flex: 1; /* 기본 비율 1 */
  text-align: center; /* 텍스트 중앙 정렬 */
}

.item:nth-child(3) {
  flex: 3; /* 세 번째 항목의 비율을 3으로 설정 */
}
</style>
