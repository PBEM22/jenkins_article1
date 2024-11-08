<template>
  <div>
    <div class="label">
      <span>{{ text }}</span>
    </div>
    <div>
      <InputBox :label="text" v-bind:value="modelValue" v-on:update:value="updateValue" />
    </div>
  </div>
</template>

<script setup>
import InputBox from "@/components/board/InputBox.vue";
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  text: String,
  modelValue: String // 부모에서 전달받는 v-model 값
});

const emit = defineEmits(['update:modelValue']);

// updateValue 함수 정의
function updateValue(newValue) {
  emit('update:modelValue', newValue); // 부모에게 전달
}
</script>

<style scoped>
.label {
  font-size: 20px;
  font-weight: bold;
}
</style>
