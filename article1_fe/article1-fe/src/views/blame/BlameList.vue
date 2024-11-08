<script setup>
// component
import Container from "@/components/board/Container.vue";
import TopBar from "@/components/blame/TopBar.vue";
import axios from "axios";
import {onMounted, ref} from "vue";
import {useAuthStore} from "@/store/authStore.js";
import BlameLi from "@/components/blame/BlameLi.vue";
import Selection from "@/components/blame/Selection.vue";

// Jwt token value
const authStore = useAuthStore();

// 호출된 데이터
const blameBoardList = ref([]);      // 게시글
const blameReplyList = ref([]);      // 댓글
const blameReviewList = ref([]);     // 리뷰

// 데이터 호출
async function fetchBoardData() {
  try {
    const response = await axios.get(`/board`, {
      params: {isBlind: true},
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      blameBoardList.value = response.data;
      console.log("찾은 게시글", blameBoardList.value);
    } else {
      console.log("신고된 게시글을 조회하는데 실패하였습니다.", "코드", response.status);
    }
  } catch (error) {
    console.log("어라라...?", error);
  }
}

async function fetchReplyData() {
  try {
    const FIND_ALL = -999;
    const response = await axios.get(`/reply/${FIND_ALL}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      blameReplyList.value = response.data;
      console.log("찾은 댓글", blameReplyList.value);
    } else {
      console.log("신고된 댓글을 조회하는데 실패하였습니다.", "코드", response.status);
    }
  } catch (error) {
    console.log("어라라...?", error);
  }
}

async function fetchReviewData() {
  try {
    const response = await axios.get(`/review`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      blameReviewList.value = response.data.filter(review => review.reviewBlind === true);
      console.log("찾은 리뷰", blameReviewList.value);
    } else {
      console.log("신고된 리뷰를 조회하는데 실패하였습니다.", "코드", response.status);
    }
  } catch (error) {
    console.log("어라라...?", error);
  }
}

onMounted(() => {
  fetchBoardData();
  fetchReplyData();
  fetchReviewData();
});

// 현재 선택된 타입
const selectedType = ref('게시글'); // 기본값 설정
</script>

<template>
  <Container>
    <div class="header">
      <Selection
          :options="[
          { value: '게시글', text: '게시글' },
          { value: '댓글', text: '댓글' },
          { value: '리뷰', text: '리뷰' }
        ]"
          :selected="selectedType"
          @update:selected="selectedType = $event"
      />
    </div>
    <TopBar/>
    <table>
      <tbody>
      <template v-if="selectedType === '게시글'">
        <tr v-for="item in blameBoardList" :key="item.boardSeq">
          <BlameLi
              :seq="item.boardSeq"
              type="게시글"
              :content="item.boardTitle"
              :writer="item.userSeq"
              :status="item.boardIsBlind.toString()"
              @update:status="newStatus => {
              if (newStatus === 'false' && blameBoardList.value) {
                blameBoardList.value = blameBoardList.value.filter(b => b.boardSeq !== item.boardSeq);
              }
              item.boardIsBlind = (newStatus === 'true');
            }"
          />
        </tr>
      </template>
      <template v-else-if="selectedType === '댓글'">
        <tr v-for="item in blameReplyList" :key="item.replySeq">
          <BlameLi
              :seq="item.replySeq"
              type="댓글"
              :content="item.replyContent"
              :writer="item.replyUserSeq"
              :status="item.replyIsBlind.toString()"
              @update:status="newStatus => {
              if (newStatus === 'false' && blameReplyList.value) {
                blameReplyList.value = blameReplyList.value.filter(r => r.replySeq !== item.replySeq);
              }
              item.replyIsBlind = (newStatus === 'true');
            }"
          />
        </tr>
      </template>
      <template v-else>
        <tr v-for="item in blameReviewList" :key="item.reviewSeq">
          <BlameLi
              :seq="item.reviewSeq"
              type="리뷰"
              :content="item.reviewContent"
              :writer="item.userSeq"
              :status="item.reviewBlind.toString()"
              @update:status="newStatus => {
              if (newStatus === 'false' && blameReviewList.value) {
                blameReviewList.value = blameReviewList.value.filter(r => r.reviewSeq !== item.reviewSeq);
              }
              item.reviewBlind = (newStatus === 'true');
            }"
          />
        </tr>
      </template>
      </tbody>
    </table>
  </Container>
</template>

<style scoped>
.header {
  display: flex; /* Flexbox 사용 */
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-bottom: 16px; /* 아래 여백 추가 */
}

table {
  width: 100%; /* 테이블 너비 설정 */
  border-collapse: collapse; /* 테두리 겹침 제거 */
}

tr {
  border-bottom: 1px solid #ccc; /* 행 사이에 경계선 추가 */
}

td {
  padding: 8px; /* 셀 패딩 */
}
</style>
