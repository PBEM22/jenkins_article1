<script setup>
import Container from "@/components/board/Container.vue";
import RegisterInput from "@/components/board/RegisterInput.vue";
import FileUpload from "@/components/board/FileUpload.vue";
</script>

<template>
  <div id="background">
    <Container>
      <div>
        <RegisterInput
            text="제목"
        />
      </div>
      <div>
        <RegisterInput
            text="내용"
        />
      </div>
      <div>
        <FileUpload
            label="이미지 업로드"
        />
      </div>
    </Container>
  </div>
</template>

<style scoped>
#background {
  background: #E7F4FF;
  height: auto; /* 높이를 자동으로 설정 */
  overflow: auto; /* 스크롤이 가능하도록 설정 */
}
</style>
