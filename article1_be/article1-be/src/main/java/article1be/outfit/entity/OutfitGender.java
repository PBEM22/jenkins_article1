package article1be.outfit.entity;

import lombok.Getter;

@Getter
public enum OutfitGender {
    M("남성용"),
    F("여성용"),
    N("성별무관");

    private final String gender;

    OutfitGender(String gender) {
        this.gender = gender;
    }
}
