package project.entrust.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSets extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_sets_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_items_id")
    private FavoriteItems favoriteItems;


    /* 연관관계 메소드 */
    public void addFavoriteItems(FavoriteItems favoriteItems) {
        this.favoriteItems = favoriteItems;
        favoriteItems.getItemSets().add(this);
    }
}
