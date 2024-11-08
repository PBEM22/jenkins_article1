<script setup>
import {computed, nextTick, ref} from 'vue';
import router from "@/router/index.js";
import {useAuthStore} from "@/store/authStore.js";

// 상태관리
const store = useAuthStore();

// 로그인 유무
const isLogIn = computed(() => store.accessToken !== null);

// 로그아웃 후 메인페이지로 이동
const logout = async () => {
  await store.logout();
  await nextTick();      // 상태가 반영된 후 페이지 이동
  await router.push('/');
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
        <li>OUTFIT</li>
        <li>REVIEW</li>
        <li>BOARD</li>
        <li>MYPAGE</li>
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
  .header-content{
    display: flex;
    justify-content: space-between;
    font-weight: bold;
  }
  .logo{
    margin-left: 2%;
  }
  .logo:hover{
    cursor: pointer;
    scale: 120%;
  }
  .menu-list{
    width: 40%;
    align-content: center;
  }
  ul{
    padding: 0;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  li{
    margin: 0 auto;
    align-content: center;
    text-align: center;
    list-style: none;
  }
  li:hover{
    cursor: pointer;
    scale: 120%;
  }
  .login-logout{
    margin-right: 2%;
    align-content: center;
    font-size: 1.2em;
  }
  .login-logout button{
    border: none;
    background-color: rgb(0, 0, 0, 0);
    font-size: 1.2em;
    font-weight: bold;
  }
  .login-logout button:hover{
    cursor: pointer;
    scale: 120%;
  }
  .signup-btn{
    margin-right: 20px;
  }
</style>