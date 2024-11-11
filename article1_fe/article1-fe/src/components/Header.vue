<script setup>
import {computed, nextTick, ref} from 'vue';
import router from "@/router/index.js";
import {useAuthStore} from "@/store/authStore.js";

// 상태관리
const store = useAuthStore();

// 로그인 유무
const isLogIn = computed(() => store.accessToken !== null);

// 관리자 권한 여부
const isAdmin = computed(() => {
  if (!store.isInitialized) {
    console.log("[Header] Store not initialized yet."); // 초기화 대기
    return false;
  }
  console.log("[Header] Current user role:", store.userRole); // 디버깅 로그
  return store.userRole === 'ADMIN';
});


// 로그아웃 후 메인페이지로 이동
const logout = async () => {
  console.log("logout 호출");
  await store.logout();
  deleteCookie('token'); // 쿠키 삭제
  await nextTick(); // 상태가 반영된 후 페이지 이동
  router.push('/');
};

function deleteCookie(name) {
  document.cookie = `${name}=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  if (document.domain) {
    document.cookie = `${name}=; Path=/; Domain=${document.domain}; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  }
}

// 메뉴 이동
function moveToItem(move) {
  console.log("[Header] Moving to menu:", move); // 디버깅 로그
  let item;
  switch (move) {
    case 'outfit':
      item = '/map';
      break;
    case 'review':
      item = isAdmin.value ? '/admin/review' : '/review';
      break;
    case 'board':
      item = '/board';
      break;
    case 'mypage':
      item = '/mypage/detail';
      break;
    case 'blame':
      item = '/blame';
      break;
    case 'user':
      item = '/admin/user';
      break;
    default:
      item = '/';
      break;
  }
  router.push(`${item}`);
}
</script>


<template>
  <div class="header-content">
    <div class="logo" @click="router.push('/')">
      <img src="@/assets/images/Logo.png" width="110px">
    </div>
    <!-- 로그인 상태일 때만 메뉴 항목 표시 -->
    <div class="menu-list" v-if="isLogIn">
      <ul>
        <li @click="moveToItem('outfit')">OUTFIT</li>
        <li @click="moveToItem('review')">REVIEW</li>
        <li @click="moveToItem('board')">BOARD</li>
        <li @click="moveToItem('mypage')">MYPAGE</li>
        <!-- 관리자만 BLAME과 USER 메뉴를 볼 수 있음 -->
        <li v-if="isAdmin" @click="moveToItem('blame')">BLAME</li>
        <li v-if="isAdmin" @click="moveToItem('user')">USER</li>
      </ul>
    </div>
    <!-- 로그인/로그아웃 버튼 -->
    <div class="login-logout">
      <template v-if="isLogIn">
        <!-- 로그아웃 버튼 -->
        <button @click="logout" class="logout-btn">LOGOUT</button>
      </template>
      <template v-else>
        <!-- 로그인 버튼 -->
        <button @click="router.push('/login')" class="login-btn">LOGIN</button>
      </template>
    </div>
  </div>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
}
.logo {
  margin-left: 2%;
}
.logo:hover {
  cursor: pointer;
  scale: 120%;
}
.menu-list {
  width: 40%;
  align-content: center;
}
ul {
  padding: 0;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
li {
  margin: 0 auto;
  align-content: center;
  text-align: center;
  list-style: none;
}
li:hover {
  cursor: pointer;
  scale: 120%;
}
.login-logout {
  margin-right: 2%;
  align-content: center;
  font-size: 1.2em;
}
.login-logout button {
  border: none;
  background-color: rgb(0, 0, 0, 0);
  font-size: 1.2em;
  font-weight: bold;
}
.login-logout button:hover {
  cursor: pointer;
  scale: 120%;
}
</style>
