import MapWithDateView from "@/views/outfit/setting/MapWithDateView.vue";
import SelectSituation from "@/views/outfit/setting/SituationView.vue";
import GuestOutfitRecommendation from "@/components/outfit/recommend/GuestOutfitRecommendation.vue";
import UserOutfitRecommendation from "@/components/outfit/recommend/UserOutfitRecommendation.vue";

export default [
    {
        path: "/map",
        name: "MapWithDateView",
        component: MapWithDateView,
    },
    {
        path: "/map/select-situation",
        name: "SelectSituation",
        component: SelectSituation,
    },
    {
        path: "/guest/outfit/recommendation",
        name: "GuestOutfitRecommendation",
        component: GuestOutfitRecommendation,
    },
    {
        path: "/user/outfit/recommendations", // 회원용 복장 추천 경로 추가
        name: "UserOutfitRecommendation",
        component: UserOutfitRecommendation,
    },
];