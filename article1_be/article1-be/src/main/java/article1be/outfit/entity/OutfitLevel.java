package article1be.outfit.entity;


import lombok.Getter;

@Getter
public enum OutfitLevel {
    REQUIRED("필수"),
    RECOMMENDED("권장"),
    SELECTION("선택");


   private final String level;

   OutfitLevel(String level) {this.level = level;}

}
