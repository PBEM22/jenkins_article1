<script setup>
import {defineEmits, defineProps, ref} from 'vue';
import axios from "axios";
import {useAuthStore} from "@/store/authStore.js"; // authStore 가져오기

const props = defineProps({
  seq: Number,
  type: String,
  content: String,
  writer: Number,
  status: String // status는 문자열로 받아옴
});

const emit = defineEmits(['update:status']); // 이벤트 정의
const authStore = useAuthStore(); // authStore 초기화

// 상태 변경 요청 함수
const updateStatus = async (newStatus) => {
  if (newStatus === 'false') { // 상태가 false로 변경될 때만 요청
    let url;
    const data = ref({})

    // type에 따라 URL 설정
    switch (props.type) {
      case '게시글':
        url = `/board/release/${props.seq}`; // 경로 수정
        break;
      case '댓글':
        url = `/reply/release/${props.seq}`;
        break;
      case '리뷰':
        url = `/review/admin/review/status`;
        data.value = {
          reviewSeq: props.seq,
          reviewBlind: false
        };
        break;
      default:
        console.error('유효하지 않은 type입니다.');
        return;
    }

    try {
      const response = await axios.put(url, data, {
        headers: {
          Authorization: `Bearer ${authStore.accessToken}`
        }
      });

      if (response.status === 200) {
        console.log('상태가 성공적으로 업데이트되었습니다.');
      } else {
        console.error('상태 업데이트에 실패했습니다.', response.status);
      }
    } catch (error) {
      console.error('상태 업데이트 중 오류가 발생했습니다.', error);
    }
  }

  emit('update:status', newStatus); // 상태 변경 이벤트 발생
};
</script>

<template>
  <div class="review-card">
    <div class="item">{{ seq }}</div>
    <div class="item">{{ type }}</div>
    <div class="item">{{ content }}</div>
    <div class="item">{{ writer }}</div>
    <div class="item">
      <select :value="status" @change="event => updateStatus(event.target.value)">
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
