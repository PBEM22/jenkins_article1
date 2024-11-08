<template>
  <div class="container">
    <h2>전체 회원 조회</h2>
    <div class="search-bar">
      <select v-model="searchField">
        <option value="전체">전체</option>
        <option value="userName">이름</option>
        <option value="userNickname">닉네임</option>
        <option value="userPhoneNum">전화 번호</option>
        <option value="userBirthDate">생년월일</option>
        <option value="userGender">성별</option>
        <option value="userState">활동 상태</option>
        <option value="userAuth">권한</option>
      </select>
      <input v-model="searchTerm" placeholder="검색어를 입력하세요" />
    </div>

    <table>
      <thead>
      <tr>
        <th>이름</th>
        <th>닉네임</th>
        <th>전화 번호</th>
        <th>생년월일</th>
        <th>성별</th>
        <th>활동 상태</th>
        <th>권한</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in filteredUsers" :key="user.userSeq">
        <td>
          <router-link :to="{ name: 'AdminDetail', params: { userSeq: user.userSeq } }">
            {{ user.userName }}
          </router-link>
        </td>
        <td>{{ user.userNickname }}</td>
        <td>{{ user.userPhoneNum }}</td>
        <td>{{ user.userBirthDate }}</td>
        <td>{{ user.userGender }}</td>
        <td :class="statusClass(user.userState)">{{ user.userState }}</td>
        <td :class="authClass(user.userAuth)">{{ user.userAuth }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';
import {useAuthStore} from "@/store/authStore.js";
import { useRouter } from 'vue-router';

export default {
  setup() {

    const userList = ref([]);
    const searchField = ref('전체');
    const searchTerm = ref('');
    const authStore = useAuthStore();
    const router = useRouter();

    const fetchUsers = async () => {
      // accessToken이 없는 경우를 확인
      if (!authStore.accessToken) {
        console.error("토큰이 없습니다. 로그인 후 다시 시도해 주세요.");
        alert("로그인이 필요합니다.");
        router.push('/login');
        return;
      }

      try {
        const response = await axios.get('/admin/user', {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });
        userList.value = Array.isArray(response.data) ? response.data : [];
      } catch (error) {
        console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
        userList.value = [];
      }
    };

    const filteredUsers = computed(() => {
      return userList.value.filter(user => {
        if (searchField.value === '전체') {
          return Object.values(user).some(value =>
              String(value).toLowerCase().includes(searchTerm.value.toLowerCase())
          );
        }
        return String(user[searchField.value])?.toLowerCase().includes(searchTerm.value.toLowerCase());
      });
    });

    const statusClass = (status) => {
      return {
        ACTIVE: 'status-active',
        BAN: 'status-ban',
        DELETE: 'status-delete'
      }[status];
    };

    const authClass = (auth) => {
      return auth === '관리자' ? 'auth-admin' : '';
    };

    onMounted(() => {
      fetchUsers();
    });

    return {
      userList,
      searchField,
      searchTerm,
      filteredUsers,
      statusClass,
      authClass
    };
  }
};
</script>

<style scoped>
.container {
  background-color: #f3f3f3;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  margin: auto;
}

h2 {
  margin-bottom: 20px;
  font-weight: bold;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  justify-content: flex-end;
}

select, input {
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 6px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

th, td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

th {
  background-color: #e6f2ff;
  font-weight: bold;
  color: black;
}

tbody tr:hover {
  background-color: #f1f1f1;
}

.status-active {
  color: blue;
}

.status-ban {
  color: gray;
}

.status-delete {
  color: red;
}

.auth-admin {
  font-weight: bold;
  color: black;
}
</style>
