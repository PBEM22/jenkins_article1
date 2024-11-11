import {defineStore} from "pinia";
import {onMounted, ref} from "vue";

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(null);
    const userRole = ref(null);
    const isLoggedIn = ref(false);
    const isInitialized = ref(false); // 상태 초기화 확인용

    // JWT 토큰 디코딩 유틸리티 함수
    function decodeToken(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            console.log("[AuthStore] Decoded payload:", payload); // 디코딩된 payload 확인
            return payload;
        } catch (error) {
            console.error("[AuthStore] Error decoding token:", error);
            return null;
        }
    }

    // 상태 초기화
    onMounted(() => {
        const token = localStorage.getItem('accessToken');
        console.log("[AuthStore] Loaded token:", token); // 로컬 스토리지에서 토큰 확인
        if (token) {
            const payload = decodeToken(token);
            if (payload) {
                accessToken.value = token;
                userRole.value = payload.auth[0]; // `auth`에서 첫 번째 값 저장
                console.log("[AuthStore] Extracted user role:", userRole.value); // 역할 디버깅
                isLoggedIn.value = true;
            }
        }
        isInitialized.value = true; // 초기화 완료
    });

    // 로그인 처리
    function login(token) {
        console.log("[AuthStore] Login token received:", token); // 로그인 디버깅
        isLoggedIn.value = true;
        accessToken.value = token;
        localStorage.setItem('accessToken', token);

        const payload = decodeToken(token);
        if (payload) {
            userRole.value = payload.auth[0]; // `auth`에서 역할 설정
            console.log("[AuthStore] Extracted user role on login:", userRole.value); // 디버깅
        }
    }

    // 로그아웃 처리
    function logout() {
        console.log("[AuthStore] Logout called"); // 로그아웃 디버깅
        isLoggedIn.value = false;
        accessToken.value = null;
        userRole.value = null;
        localStorage.removeItem('accessToken');
    }

    // 권한 확인
    function isAuthorized(requiredRole) {
        console.log("[AuthStore] Checking authorization for role:", requiredRole); // 권한 확인 디버깅
        if (!userRole.value) return false;
        return userRole.value === requiredRole; // 역할 비교
    }

    return {
        accessToken,
        userRole,
        login,
        logout,
        isAuthorized,
        isLoggedIn,
        isInitialized, // 상태 초기화 확인용
    };
});
