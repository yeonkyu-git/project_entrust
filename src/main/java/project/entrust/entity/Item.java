package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.BaseEntity;
import project.entrust.entity.assistant.ItemStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private LocalDate useStartDate;
    private LocalDate useReturnDate;
    private int useCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDate lastDeliveryAt;


    /* 연관관계 메소드 */
    public void addMember(Member member) {
        this.owner = member;
        member.getItems().add(this);
    }

}