<script setup>
import { ref } from 'vue';
import PreferenceForm from "@/components/user/PreferenceForm.vue";
import axios from 'axios';
import { useRouter } from "vue-router";

const router = useRouter();

// 닉네임과 선호도 상태 관리
const nickname = ref('');
const conditionSeq = ref(null);
const styleSeq = ref(null);

// 선호도 업데이트
const handleUpdatePreference = (preferenceData) => {
  console.log('선호도 업데이트:', preferenceData);
  conditionSeq.value = preferenceData.conditionSeq;
  styleSeq.value = preferenceData.styleSeq;
};

// 사용자 등록 처리
const handleRegisterUser = async () => {
  // 닉네임, 체질, 스타일 값 모두 유효한지 체크
  if (!nickname.value || conditionSeq.value === null || styleSeq.value === null) {
    alert('모든 필드를 입력해주세요.');
    return;
  }

  try {
    const userData = {
      userNickname: nickname.value,
      conditionSeq: conditionSeq.value,
      styleSeq: styleSeq.value
    };

    // 서버에 사용자 정보와 선호도 정보 함께 등록
    await axios.post('http://localhost:8080/user/data', userData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`
      }
    });

    alert('사용자 정보와 선호도가 등록되었습니다.');
    router.push("/")
  } catch (error) {
    console.error('등록 실패:', error);
    alert('등록에 실패했습니다.');
  }
};
</script>

<template>
  <div class="user-registration-form">
    <div class="extra-data-title">
      <h1 style="font-size: 40px">추가 정보 입력</h1>
      <button @click="handleRegisterUser">등록</button>
    </div>

    <div class="form-content">
      <!-- 닉네임 입력 -->
      <div class="nickname">
        <label for="nickname"><h2>닉네임</h2></label>
        <input
            id="nickname"
            type="text"
            v-model="nickname"
            placeholder="닉네임을 입력하세요"
            required
        />
      </div>

      <div style="padding: 10px 35px 10px 35px">
        <hr>
      </div>

      <!-- 선호도 입력 폼 -->
      <PreferenceForm
          :conditionSeq="conditionSeq"
          :styleSeq="styleSeq"
          @updatePreference="handleUpdatePreference"
      />
    </div>
  </div>
</template>

<style scoped>
.user-registration-form {
  background-color: #f0f2f5;
  border-radius: 16px;
  padding: 20px 30px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  max-width: 1000px; /* 폼의 최대 너비를 제한 */
  margin: 0 auto;  /* 화면 중앙에 배치 */
}

.extra-data-title {
  padding: 0 20px;                  /* 좌우 패딩 추가 */
  display: flex;
  justify-content: space-between;   /* 왼쪽은 제목, 오른쪽은 수정 버튼 */
  align-items: center;              /* 세로 중앙 정렬 */
  margin-bottom: 20px;              /* 제목과 내용 사이의 간격 */
}

button {
  padding: 10px 25px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 17px;
}

button:hover {
  background-color: #0056b3;
}

.nickname {
  padding-left: 35px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.nickname label {
  font-size: 18px;
  margin-right: 20px; /* 라벨과 입력 칸 사이의 간격 */
}

.nickname input {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 8px;  /* 둥근 모서리 */
  border: 1px solid #ccc;
  box-sizing: border-box;
  max-width: 300px; /* 최대 너비를 설정하여 크기를 제한 */
}

.nickname input::placeholder {
  color: #aaa;
}

h2 {
  font-size: 1.3em;
  margin-bottom: 10px;
}
</style>
