<template>
  <div class="date-picker-wrapper">
    <Datepicker
        v-model="selectedDate"
        :min-date="minDate"
        :max-date="maxDate"
        :inline="true"
        :format="displayDateFormat"
        locale="ko"
        time-picker-inline
        @update:model-value="handleDateChange"
    />
  </div>
</template>

<script>
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

export default {
  components: {
    Datepicker,
  },
  data() {
    const today = new Date();
    const maxDate = new Date(today);
    maxDate.setDate(today.getDate() + 5);

    return {
      selectedDate: today,
      minDate: today,
      maxDate: maxDate,
      displayDateFormat: "yyyy년 MM월 dd일",
      buttonText: "Select", // 버튼 초기 텍스트
      isButtonDisabled: false, // 버튼 비활성화 여부
    };
  },
  methods: {
    handleDateChange() {
      // 날짜가 변경되었을 때 버튼 텍스트를 "선택 완료"로 설정하고 버튼을 비활성화
      this.buttonText = "선택 완료";
      this.isButtonDisabled = true;
      this.updateButtonText();
    },
    updateButtonText() {
      // 버튼 요소를 찾아 텍스트와 상태를 업데이트
      const button = document.querySelector(".dp__action_button.dp__action_select");
      if (button) {
        button.textContent = this.buttonText;
        button.disabled = this.isButtonDisabled;
        button.style.cursor = this.isButtonDisabled ? "default" : "pointer";
        button.style.opacity = this.isButtonDisabled ? "0.5" : "1";
      }
    },
  },
  mounted() {
     this.updateButtonText();
  },
};
</script>

<style>
.date-picker-wrapper {
  width: 470px;
  height: 470px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dp__calendar > div:first-child[role="grid"] {
  height: 300px;
  position: relative;
  top: 50%;
  transform: translateY(10%);
}

.dp__menu_inner {
  width: 460px;
  height: 370px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.dp__menu_inner > div:nth-child(1) {
  position: absolute;
  height: 350px;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
}

.dp__instance_calendar > div:nth-child(2) {
  display: flex;
  justify-content: center;
  flex-direction: row;
}
</style>
