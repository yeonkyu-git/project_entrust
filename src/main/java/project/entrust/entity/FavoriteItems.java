package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteItems extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "favorite_items_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "favoriteItems", cascade = CascadeType.ALL)
    private List<ItemSets> itemSets = new ArrayList<>();

    /* 연관관계 메소드 */
    public void addMember(Member member) {
        this.member = member;
        member.getFavoriteItems().add(this);
    }
}
