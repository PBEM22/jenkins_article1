<script setup>
// component
import Container from "@/components/board/Container.vue";
import TopBar from "@/components/blame/TopBar.vue";

// axios
import axios from "axios";

// vue
import {onMounted, ref} from "vue";

// store
import {useAuthStore} from "@/store/authStore.js";

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
      params: {
        isBlind: true
      },
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      blameBoardList.value = response.data;
      console.log("찾은 게시글")
      console.log(blameBoardList.value);
    } else {
      console.log("신고된 게시글을 조회하는데 실패하였습니다.");
      console.log("코드 " + response.status);
    }
  } catch (error) {
    console.log("어라라...?");
    console.log(error);
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
      console.log("찾은 댓글")
      console.log(blameReplyList.value);
    } else {
      console.log("신고된 댓글을 조회하는데 실패하였습니다.");
      console.log("코드 " + response.status);
    }
  } catch (error) {
    console.log("어라라...?");
    console.log(error);
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
      console.log("찾은 리뷰")
      console.log(blameReviewList.value);
    } else {
      console.log("신고된 리뷰를 조회하는데 실패하였습니다.");
      console.log("코드 " + response.status);
    }
  } catch (error) {
    console.log("어라라...?");
    console.log(error);
  }
}

onMounted(() => {
  fetchBoardData();
  fetchReplyData();
  fetchReviewData()
});
</script>

<template>
  <Container>
    <TopBar/>
  </Container>

</template>

<style scoped>

</style>