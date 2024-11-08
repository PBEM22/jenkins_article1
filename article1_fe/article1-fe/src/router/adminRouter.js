import Admin from "@/views/admin/Admin.vue";
import AdminDetail from "@/views/admin/AdminDetail.vue";
import AdminReview from "@/views/admin/AdminReview.vue";

export default [
    {
        path: '/admin/user',
        name: 'Admin',
        component: Admin,
    },
    {
        path: '/admin/user/:userSeq',
        name: 'AdminDetail',
        component: AdminDetail,
        props: true,
    },
    {
        path: '/admin/review',
        name: 'AdminReview',
        component: AdminReview,
    }
];
