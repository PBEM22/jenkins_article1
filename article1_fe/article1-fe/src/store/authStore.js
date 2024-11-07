import {defineStore} from "pinia";
import {onMounted, ref} from "vue";

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(null);
    const userRole = ref(null);

    onMounted(() => {
        const token = localStorage.getItem('accessToken');
        console.log(token);
        if (token) {
            accessToken.value = token;
            accessToken.value = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMSIsImF1dGgiOlsiQURNSU4iXSwiZXhwIjoxNzMwOTgyODU1fQ._hu3hnWJlWzNFIbx6bhS8I63fNjTOOYJYiPWV4xjtstarroQxH-IxpilZAMHxMhhN6NGtPG0DEjcc7WpmWpr4g";
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