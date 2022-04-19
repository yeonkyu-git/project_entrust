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

    private long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    // 생성 메소드 //
    public ItemImage(String originFileName, String storedFileName, long fileSize, ItemImageType imageType) {
        this.originFileName = originFileName;
        this.storedFileName = storedFileName;
        this.fileSize = fileSize;
        this.imageType = imageType;
    }


    // 연관관계 메소드 //
    public void addItem(Item item) {
        this.item = item;
    }

}
