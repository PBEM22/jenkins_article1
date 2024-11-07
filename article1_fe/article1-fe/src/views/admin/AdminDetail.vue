<template>
  <div class="detail-container" v-if="user">
    <h2>회원 정보</h2>
    <div>
      <p><strong>아이디</strong>: {{ user.userId }}</p>
      <p><strong>이름</strong>: {{ user.userName }}</p>

      <label><strong>닉네임</strong></label>
      <input v-model="user.userNickname" />

      <p><strong>핸드폰</strong>: {{ user.userPhoneNum }}</p>
      <p><strong>생년월일</strong>: {{ user.userBirthDate }}</p>
      <p><strong>성별</strong>: {{ user.userGender }}</p>

      <label><strong>활동 상태</strong></label>
      <select v-model="user.userState">
        <option value="ACTIVE">ACTIVE</option>
        <option value="BAN">BAN</option>
        <option value="DELETE">DELETE</option>
      </select>

      <label><strong>권한</strong></label>
      <select v-model="user.userAuth">
        <option value="관리자">관리자</option>
        <option value="회원">회원</option>
      </select>

      <button @click="saveUser">저장</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const user = ref(null);
    const route = useRoute();
    const router = useRouter();
    const userSeq = route.params.userSeq;

    const fetchUser = async () => {
      try {
        const response = await axios.get(`/admin/user/${userSeq}`);
        user.value = response.data;
      } catch (error) {
        console.error('사용자 정보를 불러오는 중 오류가 발생했습니다.', error);
      }
    };

    const saveUser = async () => {
      try {
        await axios.put(`/admin/user/${userSeq}`, {
          userSeq: userSeq,
          userNickname: user.value.userNickname,
          userAuth: user.value.userAuth,
          userState: user.value.userState
        });
        alert("사용자 정보가 저장되었습니다.");
        router.push('/admin');  // 저장 후 목록 페이지로 이동
      } catch (error) {
        console.error('사용자 정보를 저장하는 중 오류가 발생했습니다.', error);
        alert("저장에 실패했습니다.");
      }
    };

    onMounted(() => {
      fetchUser();
    });

    return {
      user,
      saveUser
    };
  }
};
</script>

<style scoped>
.detail-container {
  background-color: #f3f3f3;
  padding: 20px;
  border-radius: 8px;
  width: 50%;
  margin: auto;
}

h2 {
  font-weight: bold;
}

label {
  margin-top: 10px;
}

input, select {
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 6px 12px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
}

button:hover {
  background-color: #555;
}
</style>
