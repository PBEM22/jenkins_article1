<script setup>

// Component
import Container from "@/components/board/Container.vue";
import BoardNoticeLi from "@/components/board/BoardNoticeLi.vue";
import BoardLi from "@/components/board/BoardLi.vue";
import Pagination from "@/components/common/Pagination.vue";

// Vue
import {computed, ref} from "vue";

// 테스트 데이터
const boardList = ref([
  {
    boardSeq: 1,
    userSeq: 123,
    boardTitle: "제목 1",
    boardContent: "내용 1",
    boardPictureList: [
      {
        pictureUrl: null,
        description: "이미지 설명 1"
      }
    ],
    regDate: "2023-10-31T10:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 2,
    userSeq: 124,
    boardTitle: "제목 2",
    boardContent: "내용 2",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 2"
      },
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "추가 이미지 설명 2"
      }
    ],
    regDate: "2023-11-01T11:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 3,
    userSeq: 125,
    boardTitle: "제목 3",
    boardContent: "내용 3",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 3"
      }
    ],
    regDate: "2023-11-02T12:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 4,
    userSeq: 126,
    boardTitle: "제목 4",
    boardContent: "내용 4",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 4"
      }
    ],
    regDate: "2023-11-03T13:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 5,
    userSeq: 127,
    boardTitle: "제목 5",
    boardContent: "내용 5",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 5"
      },
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "추가 이미지 설명 5"
      }
    ],
    regDate: "2023-11-04T14:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: true
  },
  {
    boardSeq: 6,
    userSeq: 128,
    boardTitle: "제목 6",
    boardContent: "내용 6",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 6"
      }
    ],
    regDate: "2023-11-05T15:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 7,
    userSeq: 129,
    boardTitle: "제목 7",
    boardContent: "내용 7",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 7"
      }
    ],
    regDate: "2023-11-06T16:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: true
  },
  {
    boardSeq: 8,
    userSeq: 130,
    boardTitle: "제목 8",
    boardContent: "내용 8",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 8"
      }
    ],
    regDate: "2023-11-07T17:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 9,
    userSeq: 131,
    boardTitle: "제목 9",
    boardContent: "내용 9",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 9"
      }
    ],
    regDate: "2023-11-08T18:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  },
  {
    boardSeq: 10,
    userSeq: 132,
    boardTitle: "제목 10",
    boardContent: "이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵 이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵 이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵 이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵 저러쿵이러쿵",
    boardPictureList: [
      {
        pictureUrl: null,
        description: "이미지 설명 10"
      },
    ],
    regDate: "2023-11-09T19:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: true
  },
  {
    boardSeq: 11,
    userSeq: 1311,
    boardTitle: "제목 11",
    boardContent: "내용 11",
    boardPictureList: [
      {
        pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
        description: "이미지 설명 11"
      }
    ],
    regDate: "2024-11-05T18:00:00",
    upDate: null,
    delDate: null,
    boardIsNotice: false
  }
]);

// 페이지 관련 상태
const currentPage = ref(1); // 현재 페이지
const itemsPerPage = 10; // 페이지당 게시물 수

// 정렬된 게시물 목록 생성
const sortedBoardList = computed(() => {
  return boardList.value.sort((a, b) => {
    return new Date(b.regDate) - new Date(a.regDate); // regDate 기준으로 정렬
  });
});

// 공지사항 목록 생성
const noticeList = computed(() => {
  const notices = sortedBoardList.value.filter(item => item.boardIsNotice);
  return notices.slice(0, 2); // 최신 2개만 반환
});

// 일반 게시물 목록 생성 (공지사항 제외)
const regularPosts = computed(() => {
  return sortedBoardList.value.filter(item => !item.boardIsNotice);
});

// 전체 게시물 수 계산
const totalPosts = computed(() => {
  return noticeList.value.length + regularPosts.value.length;
});

// 총 페이지 수 계산
const totalPages = computed(() => {
  console.log(currentPage);
  console.log(totalPosts);

  return Math.ceil(totalPosts.value / itemsPerPage); // 전체 게시물 수를 페이지당 게시물 수로 나누어 올림
});

// 페이지 변경 핸들러
const handlePageChange = (newPage) => {
  currentPage.value = newPage; // 페이지 변경
};

// 날짜 형식 변환 함수
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const today = new Date();

  // 오늘 날짜와 비교하여 동일한 경우 HH:mm 형식으로 반환
  if (
      date.getFullYear() === today.getFullYear() &&
      date.getMonth() === today.getMonth() &&
      date.getDate() === today.getDate()
  ) {
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  } else {
    // yyyy-MM-dd 형식으로 반환
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
};

</script>

<template>
  <div id="background">
    <Container>
      <table>
        <tbody>
        <!-- 공지사항 표시 -->
        <tr v-for="item in noticeList" :key="item.boardSeq">
          <BoardNoticeLi
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
        </tr>

        <!-- 일반 게시물 표시 -->
        <tr v-for="item in regularPosts" :key="item.boardSeq">
          <BoardLi
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
        </tr>
        </tbody>
      </table>
      <!-- 페이지네이션 컴포넌트 -->
      <div class="pagination-container">
        <Pagination
            :currentPage="currentPage.value"
            :totalPages="totalPages"
            @onPageChange="handlePageChange"
        />
      </div>
    </Container>
  </div>
</template>

<style scoped>
#background {
  background: #E7F4FF;
  height: 2000vh;
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  overflow: hidden;
}

tbody tr:hover {
  background-color: #f1f1f1;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px; /* 페이지네이션과 테이블 사이의 여백 */
}

.pagination button {
  padding: 10px 15px; /* 버튼 패딩 */
  margin: 0 5px; /* 버튼 간격 */
  border: 1px solid #007bff; /* 버튼 테두리 색상 */
  background-color: white; /* 버튼 배경색 */
  color: #007bff; /* 버튼 글자 색상 */
  cursor: pointer; /* 커서 스타일 */
  border-radius: 5px; /* 버튼 테두리 둥글기 */
  transition: background-color 0.3s, color 0.3s; /* 부드러운 전환 효과 */
}

.pagination button:hover {
  background-color: #007bff; /* 마우스 오버 시 배경색 */
  color: white; /* 마우스 오버 시 글자색 */
}

.pagination button:disabled {
  color: gray; /* 비활성화된 버튼 색상 */
  cursor: not-allowed; /* 비활성화된 버튼 커서 스타일 */
  border: 1px solid gray; /* 비활성화된 버튼 테두리 색상 */
}

.pagination button.active {
  font-weight: bold; /* 활성화된 페이지 버튼 강조 */
  background-color: #007bff; /* 활성화된 버튼 배경색 */
  color: white; /* 활성화된 버튼 글자색 */
}
</style>