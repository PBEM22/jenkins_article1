<template>
  <div class="file-upload">
    <div class="label">
      <span>이미지 업로드</span>
    </div>
    <input
        :id="inputId"
        type="file"
        @change="handleFileUpload"
        accept="image/*"
        multiple
    />
  </div>
</template>

<script setup>
import { ref, defineProps } from 'vue';

const props = defineProps({
  label: {
    type: String,
    required: true
  }
});

const inputId = `file-upload-${Math.random().toString(36).substr(2, 9)}`; // 고유 ID 생성

function handleFileUpload(event) {
  const files = event.target.files; // 선택된 모든 파일
  if (files.length > 0) {
    // 선택된 파일을 배열로 변환하여 처리
    Array.from(files).forEach(file => {
      console.log("Uploaded file:", file);
      // 파일 업로드 처리 로직 (예: 서버에 전송) 추가 가능
    });
  }
}
</script>

<style scoped>
.file-upload {
  margin-top: 20px; /* 여백 추가 */
}

.label {
  font-size: 20px;
  font-weight: bold;
}

input[type="file"] {
  padding: 10px; /* 패딩 추가 */
}
</style>
