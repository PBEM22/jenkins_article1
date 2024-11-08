package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outfit")
@ToString
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outfitSeq;

    private String outfitName;

    private Integer outfitWeather; // 악세사리 외에는 null

    private Double outfitTempMax;

    private Double outfitTempMin;

    @Enumerated(EnumType.STRING)
    private OutfitGender outfitGender; // 악세사리는 null

    @Enumerated(EnumType.STRING)
    private OutfitLevel outfitLevel; // 악세사리 외에는 null

    @Enumerated(EnumType.STRING)
    private OutfitCategory outfitCategory;

}