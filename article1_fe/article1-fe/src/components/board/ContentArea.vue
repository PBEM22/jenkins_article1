<script setup>

// vue
import {defineEmits} from "vue";

// route
import {useRoute} from "vue-router";

// store
import {useAuthStore} from "@/store/authStore.js";

import axios from "axios";

const props = defineProps({
  boardTitle: String,
  boardContent: String,
  boardPictureList: {
    type: Array,
    default: () => [] // 기본값 설정 (선택 사항)
  }
});

const authStore = useAuthStore();

const route = useRoute();

const emit = defineEmits(["delete"]);

// 신고 버튼 클릭 이벤트
async function blameBoard() {
  try {
    const boardSeq = route.params.boardSeq;
    const response = await axios.post(`http://localhost:8080/blame/board/${boardSeq}`, {}, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      }
    });

    if (response.status === 200) {
      alert(`신고가 완료되었습니다.`);
      console.log("코드 = " + response.status)
    } else {
      alert(`신고에 실패했습니다.`);
      console.log("코드 = " + response.status)
    }
  } catch (error) {
    console.log("게시글 신고 중 오류가 발생했습니다.");
    console.log(error);
  }
}

// 삭제 버튼 클릭 이벤트
function deleteBoard() {
  emit("delete"); // 부모에게 삭제 이벤트를 전달
}
</script>

<template>
  <div>
    <!-- 제목 -->
    <div>
      <span class="board-title">{{ boardTitle }}</span>
    </div>
    <!-- 내용 -->
    <div>
      <span class="board-content">{{ boardContent }}</span>
    </div>
    <!-- 사진들 -->
    <div>
      <table class="centered-table">
        <tbody>
        <tr>
          <td v-for="(picture, index) in boardPictureList" :key="index">
            <img :src="picture.pictureUrl" :alt="picture.description" class="board-image"/>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <!-- 신고 / 삭제 -->
    <div class="blame-delete">
      <span class="blame-option" v-on:click="blameBoard">신고하기</span>
      <span class="blame-option" v-on:click="deleteBoard">삭제하기</span>
    </div>
  </div>
</template>

<style scoped>
.board-title {
  font-weight: bold;
  font-size: 20px;
}

.board-content {
  margin-top: 2%;
}

.centered-table {
  margin: 0 auto; /* 테이블 가운데 정렬 */
  border-collapse: collapse; /* 테두리 간격 제거 */
}

.board-image {
  max-width: 100px; /* 이미지 크기 조정 */
  height: auto; /* 비율 유지 */
}

.blame-delete {
  display: flex; /* Flexbox 사용 */
  justify-content: flex-end; /* 오른쪽 정렬 */
  color: #cccccc;
  font-size: 11px;
  margin-top: 10%; /* 위쪽 여백 추가 (선택 사항) */
  margin-right: 8px;
}

.blame-option {
  margin-left: 10px; /* 간격 추가 */
}
</style>
