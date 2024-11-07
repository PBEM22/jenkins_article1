import {defineStore} from "pinia";
import {onMounted, ref} from "vue";

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(null);
    const userRole = ref(null);

    onMounted(() => {
        const token = localStorage.getItem('accessToken');
        console.log(token);
        if (token) {
            // accessToken.value = token;
            accessToken.value = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjQiLCJhdXRoIjpbIlVTRVIiXSwiZXhwIjoxNzMwOTc4ODczfQ.14dirO3NW2YEYeBpE8AyzscoSeV_ffJ2QEL813Bi0KI4zSMvewy5qquq5RxhbfdJUqQzlQ-_vivcUFkQXRT-iA";
            const payload = JSON.parse(atob(token.split('.')[1])); // JWT 토큰의 페이로드 추출
            userRole.value = payload.auth[0].slice(5);
            console.log(userRole.value)
        }
    });

    function login(token) {
        accessToken.value = token;
        localStorage.setItem('accessToken', token);
        const payload = JSON.parse(atob(token.split('.')[1]));
        userRole.value = payload.auth[0].slice(5);
    }

    function logout() {
        accessToken.value = null;
        userRole.value = null;
        localStorage.removeItem('accessToken');
    }

    function isAuthorized(requiredRole) {
        if (!userRole.value) return false;
        return userRole.value.includes(requiredRole);
    }

    return {accessToken, userRole, login, logout, isAuthorized};
});