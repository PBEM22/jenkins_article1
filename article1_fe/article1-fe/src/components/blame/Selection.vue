<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  options: {
    type: Array,
    required: true
  },
  selected: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['update:selected']);
</script>

<template>
  <div class="selection-container">
    <label for="selection">선택:</label>
    <select id="selection" :value="selected" @change="event => emit('update:selected', event.target.value)">
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.text }}
      </option>
    </select>
  </div>
</template>

<style scoped>
.selection-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
}

select {
  margin-left: 8px;
}
</style>