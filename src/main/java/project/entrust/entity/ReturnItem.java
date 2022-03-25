package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.BaseEntity;
import project.entrust.entity.assistant.ReturnCode;
import project.entrust.entity.assistant.ReturnStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "return_item_id")
    private Long id;

    @Embedded
    private Address ReturnAddress;

    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus;

    @Enumerated(EnumType.STRING)
    private ReturnCode returnCode;

    @OneToOne(mappedBy = "returns")
    private Delivery delivery;

    private LocalDateTime returnStartDate;
    private String returnerName;
    private String returnerPhone;

    /* 연관관계 메소드 */
    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
