<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

const userData = ref({
  userSocialSite: null,
  userId: "",
  userName: "",
  userNickname: "",
  userPhoneNum: "",
  userBirthDate: "",
  userGender: "",
})

const badgeClass = computed(() => {
  switch (userData.value.userSocialSite) {
    case 'KAKAO':
      return 'badge kakao';
    case 'NAVER':
      return 'badge naver';
    case 'GOOGLE':
      return 'badge google';
    default:
      return 'badge';
  }
});

const badgeText = computed(() => {
  switch (userData.value.userSocialSite) {
    case 'KAKAO':
      return 'K';
    case 'NAVER':
      return 'N';
    case 'GOOGLE':
      return 'G';
    default:
      return 'N';
  }
});

const fetchUserData = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/user/detail`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      }
    });
    console.log("데이터:", response);
    userData.value = response.data;
  } catch (error) {
    console.error("회원정보를 불러오는 중 오류 발생 ", error);
  }
}

const editNickname = async() => {
  try {
    const response = await axios.put(`http://localhost:8080/user/nickname`, {
      userNickname: userData.value.userNickname, // 수정된 닉네임
    }, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        "Content-Type": "application/json"
      }
    });
    alert('닉네임이 정상적으로 수정되었습니다.');
    fetchUserData();  // 수정 후 최신 데이터로 갱신
  } catch(error) {
    console.error("닉네임 수정 중 오류 발생 " , error);
    alert('닉네임 수정 중 오류가 발생했습니다.');
  }
}

const deleteUser = async () => {
  // 탈퇴 확인 메시지 띄우기
  const isConfirmed = window.confirm('정말로 탈퇴하시겠습니까?');

  if (isConfirmed) {
    try {
      const response = await axios.delete(`http://localhost:8080/user`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        }
      });
      alert('회원 탈퇴가 정상적으로 완료되었습니다.');
      router.push("/") // 홈으로 리다이렉트
    } catch(error) {
      console.error("회원 탈퇴 중 오류 발생 ", error);
      alert('회원 탈퇴 실패');
    }
  } else {
    // 사용자가 취소를 클릭하면 아무 작업도 하지 않음
    console.log('회원 탈퇴가 취소되었습니다.');
  }
}

onMounted(() => {
  fetchUserData();
})
</script>

<template>
  <div class="userDetail-title">
    <h1 style="font-size: 40px">회원 정보</h1>
  </div>

  <div class="detail-container" v-if="userData">
    <div class="card">
      <div class="row">
        <div class="label">아이디</div>
        <div class="content">
          <span :class="badgeClass">{{ badgeText }}</span> {{ userData.userId }}
        </div>
      </div>

      <div class="row">
        <div class="label">이름</div>
        <div class="content">{{ userData.userName }}</div>
      </div>

      <div class="row">
        <div class="label">닉네임</div>
        <div class="content header-row">
          <input v-model="userData.userNickname" type="text" class="input nickname-input" />
          <button @click="editNickname" class="edit-button">수정</button>
        </div>
      </div>

      <div class="row">
        <div class="label">핸드폰</div>
        <div class="content">{{ userData.userPhoneNum }}</div>
      </div>

      <div class="row">
        <div class="label">생년월일</div>
        <div class="content-inline">{{ userData.userBirthDate }}</div>
      </div>

      <div class="row">
        <div class="label-inline">성별</div>
        <div class="content-inline">{{ userData.userGender }}</div>
      </div>

      <div class="row action-button">
          <button @click="deleteUser" class="delete-button">탈퇴</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.userDetail-title {
  padding: 0 20px;
}

.detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.card {
  background-color: #f0f2f5;
  border-radius: 16px;
  padding: 35px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-button {
  background-color: #555555;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 16px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-button {
  background-color: orangered;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 16px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button {
  justify-content: flex-end; /* 오른쪽 끝으로 정렬 */
}

.row, .row-inline {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px 0;
}

.label, .label-inline {
  font-weight: bold;
  font-size: 18px;
  text-align: right;
  padding-right: 20px;
  min-width: 120px;
}

.content, .content-inline {
  font-size: 18px;
  flex: 1;
}

.input{
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  outline: none;
}

.nickname-input {
  width: 50%;
}

.badge {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 4px;
  margin-right: 10px;
}

.badge.kakao {
  background-color: #fee500;
  color: #3c1e1e;
}

.badge.naver {
  background-color: #28a745;
  color: white;
}

.badge.google {
  background-color: white;
  color: #db4437;
  border: 1px solid #db4437;
}
</style>
