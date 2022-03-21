package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.BaseEntity;
import project.entrust.entity.assistant.DeliveryStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String receiverName;
    private String receiverPhone;

    @Embedded
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order;


    /* 연관관계 메소드 */
    public void addOrder(Order order) {
        this.order = order;
    }

}
