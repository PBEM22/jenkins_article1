import UserLogin from "@/views/user/UserLogin.vue";
import UserData from "@/views/user/UserData.vue";

export default [
    {
        // 로그인 화면
        path: '/login',
        component: UserLogin
    },
    {
        // 첫 로그인(회원가입) 시 추가정보 등록 화면
        path: '/user/data',
        component: UserData
    }
];
