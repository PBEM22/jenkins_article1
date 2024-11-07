import ReviewList from "@/views/review/ReviewList.vue";

export default [
    {
        path: '/review',
        name: 'ReviewList',
        component: ReviewList, // 전체 리뷰 목록 조회
        meta: { requiresAuth: false } // 인증 없이 접근 가능
    },
];
