import { createPinia } from 'pinia';
import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";
import axios from "axios";

const app = createApp(App);
const pinia = createPinia();

axios.defaults.baseURL = '/boot';
app.use(router).use(pinia).mount("#app");
