<script setup>
import {defineEmits, defineProps, ref} from 'vue';
import axios from "axios";
import {useRouter} from "vue-router"; // router 가져오기
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
const router = useRouter(); // router 초기화

// 상태 변경 요청 함수
const updateStatus = async (newStatus) => {
  let url;
  const data = ref({});

  if (newStatus === 'false') {
    // 상태를 false로 변경
    switch (props.type) {
      case '게시글':
        url = `/admin/board/setting`;
        data.value = {
          boardSeq: props.seq,
          isBlind: true
        };
        break;
      case '댓글':
        url = `/admin/reply/setting`;
        data.value = {
          replySeq: props.seq,
          isBlind: true
        };
        break;
      case '리뷰':
        url = `/admin/review/status`;
        data.value = {
          reviewSeq: props.seq,
          reviewBlind: true
        };
        break;
      default:
        console.error('유효하지 않은 type입니다.');
        return;
    }
  } else {
    // 상태를 true로 변경
    switch (props.type) {
      case '게시글':
        url = `/admin/board/setting`;
        data.value = {
          boardSeq: props.seq,
          isBlind: false
        };
        break;
      case '댓글':
        url = `/admin/reply/setting`;
        data.value = {
          replySeq: props.seq,
          isBlind: false
        };
        break;
      case '리뷰':
        url = `/admin/review/status`;
        data.value = {
          reviewSeq: props.seq,
          reviewBlind: false
        };
        break;
      default:
        console.error('유효하지 않은 type입니다.');
        return;
    }
  }

  try {
    const response = await axios.put(url, data.value, {
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

  emit('update:status', newStatus); // 상태 변경 이벤트 발생
};

// 게시글 상세 페이지로 이동
const goToDetail = () => {
  if (props.type === '게시글') {
    router.push(`/board/${props.seq}`);
  } else {
    console.error('게시글 타입이 아닙니다. 상세 페이지 이동이 지원되지 않습니다.');
  }
};
</script>

<template>
  <div class="review-card" @click="goToDetail">
    <div class="item">{{ seq }}</div>
    <div class="item">{{ type }}</div>
    <div class="item">{{ content }}</div>
    <div class="item">{{ writer }}</div>
    <div class="item">
      <select :value="status" @click.stop @change="event => updateStatus(event.target.value)">
        <option value="true">블라인드</option>
        <option value="false">해제</option>
      </select>
    </div>
  </div>
</template>

<style scoped>
.review-card {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  background-color: #f9f9f9;
  padding: 8px;
  margin: 8px 0;
  cursor: pointer; /* 클릭 가능 표시 */
  transition: background-color 0.3s;
}

.review-card:hover {
  background-color: #f0f0f0; /* 마우스 오버 시 배경 색 변경 */
}

.item {
  flex: 1;
  text-align: center;
}

.item:nth-child(3) {
  flex: 3;
}

select {
  cursor: pointer;
}
</style>
