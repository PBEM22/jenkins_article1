<template>
  <div class="detail-container" v-if="user">
    <div class="card">
      <div class="header-row">
        <h2 class="title">회원 정보</h2>
        <button @click="saveUser" class="save-button">저장</button>
      </div>

      <div class="row">
        <div class="label">아이디</div>
        <div class="content">
          <span :class="badgeClass">{{ badgeText }}</span> {{ user.userId }}
        </div>
      </div>

      <div class="row">
        <div class="label">이름</div>
        <div class="content">{{ user.userName }}</div>
      </div>

      <div class="row">
        <div class="label">닉네임</div>
        <div class="content">
          <input v-model="user.userNickname" type="text" class="input nickname-input" />
        </div>
      </div>

      <div class="row">
        <div class="label">핸드폰</div>
        <div class="content">{{ user.userPhoneNum }}</div>
      </div>

      <div class="row-inline">
        <div class="label">생년월일</div>
        <div class="content-inline">{{ user.userBirthDate }}</div>
        <div class="label-inline">성별</div>
        <div class="content-inline">{{ user.userGender }}</div>
      </div>

      <div class="row-inline">
        <div class="label">활동 상태</div>
        <div class="content-inline">
          <select v-model="user.userState" class="select small-select">
            <option value="ACTIVE">ACTIVE</option>
            <option value="BAN">BAN</option>
            <option value="DELETE">DELETE</option>
          </select>
        </div>
        <div class="label-inline">권한</div>
        <div class="content-inline">
          <select v-model="user.userAuth" class="select small-select">
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/store/authStore';

export default {
  setup() {
    const authStore = useAuthStore();
    const user = ref(null);
    const route = useRoute();
    const router = useRouter();
    const userSeq = route.params.userSeq;

    const fetchUser = async () => {
      try {
        const response = await axios.get(`/admin/user/${userSeq}`, {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        user.value = response.data;
      } catch (error) {
        console.error('사용자 정보를 불러오는 중 오류가 발생했습니다.', error);
        user.value = {};
      }
    };

    const saveUser = async () => {
      try {
        await axios.put(`/admin/user/${userSeq}`, {
          userSeq: userSeq,
          userNickname: user.value.userNickname,
          userAuth: user.value.userAuth,
          userState: user.value.userState
        }, {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        alert("사용자 정보가 저장되었습니다.");
        router.push('/admin/user');
      } catch (error) {
        console.error('사용자 정보를 저장하는 중 오류가 발생했습니다.', error);
        alert("저장에 실패했습니다.");
      }
    };

    onMounted(() => {
      fetchUser();
    });

    const badgeClass = computed(() => {
      switch (user.value?.socialSite) {
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
      switch (user.value?.socialSite) {
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

    return {
      user,
      saveUser,
      badgeClass,
      badgeText
    };
  }
};
</script>

<style scoped>
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
  margin-bottom: 25px;
}

.title {
  font-size: 24px;
  font-weight: bold;
}

.save-button {
  background-color: #555555;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 16px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.save-button:hover {
  background-color: #777777;
}

.row, .row-inline {
  display: flex;
  align-items: center;
  justify-content: flex-start;
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

.input, .select {
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

.small-select {
  width: 80%;
}
</style>
