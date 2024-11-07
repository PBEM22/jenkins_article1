<template>
  <div class="hint-input">
    <input :id="inputId" type="text" :placeholder="hint" :value="value" @input="onInput" />
  </div>
</template>

<script setup>
import {defineProps, defineEmits} from 'vue';

const props = defineProps({
  label: {
    type: String,
    required: true
  },
  value: { // modelValue 대신 value로 변경
    type: String,
    default: ''
  }
});

const emit = defineEmits(['update:value']); // update:modelValue 대신 update:value로 변경

const inputId = `input-${Math.random().toString(36).substr(2, 9)}`; // 고유 ID 생성

// 힌트 메시지 생성
const hint = `${props.label}을(를) 입력해주세요.`;

// 입력값이 변경될 때 emit
function onInput(event) {
  emit('update:value', event.target.value); // 입력값을 부모로 emit
}
</script>

<style scoped>
.hint-input {
  margin-bottom: 20px; /* 하단 여백 추가 */
}

label {
  display: block; /* 레이블을 블록 요소로 설정 */
  margin-bottom: 5px; /* 레이블과 입력 필드 간의 여백 */
}

input {
  width: 95%; /* 입력 필드의 너비를 100%로 설정 */
  padding: 10px; /* 패딩 추가 */
  border: 1px solid #ccc; /* 테두리 스타일 */
  border-radius: 4px; /* 테두리 둥글게 */
  font-size: 11px; /* 글자 크기 설정 */
}
</style>
