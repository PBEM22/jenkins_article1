<script setup>
import Header from "@/components/Header.vue";
import {ref, watchEffect} from "vue";
import {useWeatherStore} from "@/store/useWeatherStore.js";

// 1. weatherStore 사용
const weatherStore = useWeatherStore();

// 2. 상태 변수로 배경 이미지 URL을 설정
const imageUrl = ref('');

// 3. store의 backgroundImage 값에 따라 상태를 설정
const setBackgroundImage = (imageName) => {
  let url;
  switch (imageName) {
    case 'back-main.png':
      url = `url(${new URL('@/assets/images/back-main.png', import.meta.url).href})`;
      break;
    case 'back-clean.png':
      url = `url(${new URL('@/assets/images/back-clean.png', import.meta.url).href})`;
      break;
    case 'back-cloud.png':
      url = `url(${new URL('@/assets/images/back-cloud.png', import.meta.url).href})`;
      break;
    case 'back-dust.png':
      url = `url(${new URL('@/assets/images/back-dust.png', import.meta.url).href})`;
      break;
    case 'back-fog.png':
      url = `url(${new URL('@/assets/images/back-fog.png', import.meta.url).href})`;
      break;
    case 'back-rain.png':
      url = `url(${new URL('@/assets/images/back-rain.png', import.meta.url).href})`;
      break;
    case 'back-snow.png':
      url = `url(${new URL('@/assets/images/back-snow.png', import.meta.url).href})`;
      break;
    case 'back-thunder.png':
      url = `url(${new URL('@/assets/images/back-thunder.png', import.meta.url).href})`;
      break;
    default:
      url = `url(${new URL('@/assets/images/back-main.png', import.meta.url).href})`;
      break;
  }
  return url;
};

// 4. store의 backgroundImage 값을 반영하여 imageUrl 업데이트
watchEffect(() => {
  // setBackgroundImage 호출해서 imageUrl을 갱신
  imageUrl.value = setBackgroundImage(weatherStore.backgroundImage);
  // body의 배경이미지 업데이트
  document.body.style.backgroundImage = imageUrl.value;
});

// 5. 초기값 설정
imageUrl.value = setBackgroundImage(weatherStore.backgroundImage);
document.body.style.backgroundImage = imageUrl.value;
</script>

<template>
  <Header />
  <div id="app">
    <RouterView :key="$route.fullPath"/>
  </div>
</template>

<style>
  body{
    margin: 0;
    padding: 0;
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    margin-top: 0;
  }
</style>
