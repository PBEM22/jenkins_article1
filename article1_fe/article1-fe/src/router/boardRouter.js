import BoardList from "@/views/board/BoardList.vue";
import BoardDetail from "@/views/board/BoardDetail.vue";
import BoardRegister from "@/views/board/BoardRegister.vue";
import BoardModify from "@/views/board/BoardModify.vue";

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
    },
    {
        // 게시글 등록
        path: '/board/register',
        component: BoardRegister
    },
    {
        // 게시글 수정
        path: '/board/:boardSeq',
        component: BoardModify
    }
];