import Admin from "@/views/admin/Admin.vue";
import AdminDetail from "@/views/admin/AdminDetail.vue";

export default [
    {
        path: '/admin/user',
        name: 'Admin', // 이름 추가 (선택 사항)
        component: Admin, // 관리자 회원 목록 조회
    },
    {
        path: '/admin/user/:userSeq',
        name: 'AdminDetail', // 여기에 이름 추가
        component: AdminDetail, // 관리자 회원 상세 조회
        props: true
    }
];
