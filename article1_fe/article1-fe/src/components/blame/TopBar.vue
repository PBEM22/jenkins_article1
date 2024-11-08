<script setup>

</script>

<template>
  <div id="top-bar">
    <div class="item"><span>신고번호</span></div>
    <div class="item"><span>종류</span></div>
    <div class="item"><span>내용</span></div>
    <div class="item"><span>작성자</span></div>
    <div class="item"><span>처리하기</span></div>
  </div>
</template>

<style scoped>
#top-bar {
  display: flex; /* Flexbox 사용 */
  background: #cccccc;
  height: 48px;
  border: 1px solid black;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3); /* 그림자 효과 추가 */
}

.item {
  flex: 1; /* 기본 비율 1 */
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 가로 중앙 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
}

.item:nth-child(3) {
  flex: 3; /* 세 번째 div의 비율을 3으로 설정 */
}

.item span {
  font-weight: bold;
}
</style>
