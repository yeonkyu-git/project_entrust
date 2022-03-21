package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.BaseEntity;
import project.entrust.entity.assistant.ItemImageType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    private String originFileName;
    private String storedFileName;

    @Enumerated(EnumType.STRING)
    private ItemImageType imageType;

    private int fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
