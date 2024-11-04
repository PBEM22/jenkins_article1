package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "select_outfit")
public class SelectOutfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectOutfitSeq;

    private Long outfitSeq;

    private Long selectSeq;

    public static SelectOutfit create(Long selectSeq, Long outfitSeq) {
        SelectOutfit selectOutfit = new SelectOutfit();
        selectOutfit.selectSeq = selectSeq;
        selectOutfit.outfitSeq = outfitSeq;
        return selectOutfit;
    }
}
