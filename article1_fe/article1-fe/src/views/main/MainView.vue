<script setup>

</script>

<template>
  <div class="main">

    <div class="main-content">

      <div class="main-title">
        <h1>입기 좋은 날</h1>
      </div>

      <div class="main-sub_title">
        <h3>날씨와 대기상황에 맞게 옷을 골라보세요!</h3>
      </div>

      <div class="main-move_bar">
        <span class="more-content">더 알아보기</span>
        <div class="under">
          <img src="https://img.icons8.com/?size=100&id=26195&format=png&color=000000" width="30px">
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .main{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100vh;
    background-image: url('@/assets/images/back-main.png');
    background-position: center center;
    text-align: center;
  }
  h1{
    margin-top: 0;
    font-weight: bolder;
    font-size: 3em;
    margin-bottom: 5%;
  }
  .main-sub_title{
    margin-bottom: 10%;
  }
  .main-content{
    background-color: rgb(255, 255, 255, 0.5);
    width: 80%;
    height: 80%;
    border-radius: 50px;
    position: relative;
    align-content: center;
  }
  .main-move_bar{
    width: 100%;
    height: 15%;
    position: absolute;
    bottom: 0;
    background: linear-gradient(rgba(100, 156, 203, 0.4), rgba(0, 0, 0, 0.4));
    border-radius: 0 0 50px 50px;
    align-content: center;
  }
</style>