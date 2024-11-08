import mainRouter from "@/router/mainRouter.js";
import boardRouter from "@/router/boardRouter.js";
import userRouter from "@/router/userRouter.js"
import mypageRouter from "@/router/mypageRouter.js"
import adminRouter from "@/router/adminRouter.js";
import reviewRouter from "@/router/reviewRouter.js";
import {createRouter, createWebHistory} from "vue-router";
import blameRouter from "@/router/blameRouter.js";
import outfitRouter from "@/router/outfitRouter.js";

const routes = [

    // 메인 라우터
    ...mainRouter,
    // 게시판 라우터
    ...boardRouter,
    // 회원 라우터
    ...userRouter,
    // 마이페이지 라우터
    ...mypageRouter,
    // 신고 라우터
    ...blameRouter,
    // 관리자페이지 라우터
    ...adminRouter,
    // 리뷰 라우터
    ...reviewRouter,
    // 복장 추천 라우터
    ...outfitRouter
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    // 라우팅 시 화면 최 상단으로 이동됨.
    scrollBehavior(to, from, savedPosition) {
        // savedPosition이 있는 경우(예: 뒤로 가기), 해당 위치로 이동
        if (savedPosition) {
            return savedPosition;
        } else {
            // 새로운 페이지 이동 시 맨 위로 스크롤
            return {top: 0};
        }
    },
});

export default router;