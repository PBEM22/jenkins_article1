import UserLogin from "@/views/user/UserLogin.vue";
import UserData from "@/views/user/UserData.vue";
import DeletedUserError from "@/views/user/DeletedUserError.vue";
import BannedUserError from "@/views/user/BannedUserError.vue";

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
    },
    {
        // 탈퇴한 회원이 로그인 요청 시
        path: '/user/delete',
        component: DeletedUserError
    },
    {
        // 정지 당한 회원이 로그인 요청 시
        path: '/user/ban',
        component: BannedUserError
    }
];
