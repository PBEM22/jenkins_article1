package article1be.outfit.entity;

import lombok.Getter;

@Getter
public enum OutfitCategory {

    TOP("상의"),
    BOTTOM("하의"),
    OUTERWEAR("아우터"),
    SHOES("신발"),
    ACCESSORY("악세서리");

    private final String category;

    OutfitCategory(String category) {this.category = category;}

}
