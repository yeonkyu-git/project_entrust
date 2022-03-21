package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String username;
    private int age;
    private String phone;

    @Embedded
    private Address address;
    private int role;

    @OneToMany(mappedBy = "owner")
    private List<Item> items = new ArrayList<>();

    private LocalDateTime lastLoginAt;
}
