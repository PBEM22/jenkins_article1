<template>
  <div class="account-closed-container">
    <div class="message-container">
      <h1>정지된 계정입니다.</h1>
      <p>이 계정은 정지 처리된 상태입니다. 고객센터로 문의주시기 바랍니다.</p>
    </div>

    <div class="action-buttons">
      <button @click="goToLoginPage">로그인 페이지로 돌아가기</button>
      <button @click="contactSupport">고객센터 문의</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

// 로그인 페이지로 돌아가기
const goToLoginPage = () => {
  router.push('/login');
}

// 고객센터 문의
const contactSupport = () => {
  window.location.href = 'mailto:dyong0922@naver.com'; // 고객센터 이메일로 이동
}
</script>

<style scoped>
.account-closed-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.message-container {
  background-color: #fff;
  border: 1px solid #f5c6cb;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.message-container h1 {
  font-size: 24px;
  color: #721c24;
  margin-bottom: 15px;
}

.message-container p {
  font-size: 18px;
  color: #721c24;
}

.action-buttons {
  margin-top: 30px;
}

.action-buttons button {
  background-color: #007bff;
  color: #fff;
  padding: 10px 30px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin: 10px;
}

.action-buttons button:hover {
  background-color: #0056b3;
}

.action-buttons button:nth-child(2) {
  background-color: #28a745;
}

.action-buttons button:nth-child(2):hover {
  background-color: #218838;
}
</style>
