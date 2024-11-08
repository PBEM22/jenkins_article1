import {defineStore} from "pinia";
import {onMounted, ref} from "vue";

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(null);
    const userRole = ref(null);
    const isLoggedIn = ref(false);

    onMounted(() => {
        const token = localStorage.getItem('accessToken');
        console.log(token);
        if (token) {
            accessToken.value = token;
            const payload = JSON.parse(atob(token.split('.')[1]));
            userRole.value = payload.auth[0].slice(5);
            isLoggedIn.value = true;
        }
    });

    function login(token) {
        isLoggedIn.value = true;
        accessToken.value = token;
        localStorage.setItem('accessToken', token);
        const payload = JSON.parse(atob(token.split('.')[1]));
        userRole.value = payload.auth[0].slice(5);
        console.log(userRole);
    }

    function logout() {
        isLoggedIn.value = false;
        accessToken.value = null;
        userRole.value = null;
        localStorage.removeItem('accessToken');
    }

    function isAuthorized(requiredRole) {
        if (!userRole.value) return false;
        return userRole.value.includes(requiredRole);
    }

    return {accessToken, userRole, login, logout, isAuthorized, isLoggedIn};
});