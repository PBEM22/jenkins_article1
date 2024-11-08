// store/selectedInfoStore.js
import { defineStore } from "pinia";

export const useSelectedInfoStore = defineStore("selectedInfoStore", {
    state: () => ({
        selectedLatitude: null,
        selectedLongitude: null,
        selectedDate: new Date(),
        selectedSituation: null, // 선택한 상황을 저장
        selectedOutfits: {
            topSeq: null,
            bottomSeq: null,
            shoesSeq: null,
            outerSeq: null,
            accessorySeq: [],
        },
    }),
    getters: {
        isSelected: (state) => (category, outfitSeq) => {
            if (category === "ACCESSORY") {
                return state.selectedOutfits.accessorySeq.includes(outfitSeq);
            } else if (category === "OUTERWEAR") {
                return state.selectedOutfits.outerSeq === outfitSeq;
            } else {
                return state.selectedOutfits[`${category.toLowerCase()}Seq`] === outfitSeq;
            }
        },
    },
    actions: {
        setLocation(latitude, longitude) {
            this.selectedLatitude = latitude;
            this.selectedLongitude = longitude;
        },
        setDate(date) {
            this.selectedDate = date;
        },
        setSituation(situation) {
            this.selectedSituation = situation;
        },
        toggleSelection(category, outfitSeq) {
            if (category === "ACCESSORY") {
                const index = this.selectedOutfits.accessorySeq.indexOf(outfitSeq);
                if (index >= 0) {
                    this.selectedOutfits.accessorySeq.splice(index, 1);
                } else {
                    this.selectedOutfits.accessorySeq.push(outfitSeq);
                }
            } else if (category === "OUTERWEAR") {
                this.selectedOutfits.outerSeq = outfitSeq;
            } else {
                this.selectedOutfits[`${category.toLowerCase()}Seq`] = outfitSeq;
            }
        },
    },
});
