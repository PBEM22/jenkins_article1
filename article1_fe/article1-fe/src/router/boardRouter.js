import BoardList from "@/views/board/BoardList.vue";
import BoardDetail from "@/views/board/BoardDetail.vue";

export default [
    {
        // 게시글 목록
        path: '/board',
        component: BoardList
    },
    {
        // 게시글 조회
        path: '/board/:boardSeq',
        component: BoardDetail
    }
];