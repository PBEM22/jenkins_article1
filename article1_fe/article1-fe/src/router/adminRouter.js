import Admin from "@/views/admin/Admin.vue";
import AdminDetail from "@/views/admin/AdminDetail.vue";

export default [
    {
        path: '/admin/user',
        component: Admin, // 관리자 회원 목록 조회
    },
    {
        path: '/admin/user/:userSeq',
        component: AdminDetail, // 관리자 회원 상세 조회
        props: true
    }
];
