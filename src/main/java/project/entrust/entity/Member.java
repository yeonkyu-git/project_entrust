package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import project.entrust.entity.assistant.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate birthday;
    private String phone;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberShip memberShip;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "owner")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<FavoriteItems> favoriteItems = new ArrayList<>();

    private LocalDateTime lastLoginAt;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", role=" + role +
                ", memberShip=" + memberShip +
                ", lastLoginAt=" + lastLoginAt +
                '}';
    }

    /* static Method */
    public static Member createMember(String email, String password, String username, LocalDate birthday, String phone, Address address) {
        return new Member(email, password, username, birthday, phone, address);
    }

    /* 생성 메소드 */
    public Member(String email, String password, String username, LocalDate birthday, String phone, Address address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;

        // 기본 셋팅 부분
        this.role = MemberRole.NORMAL;
        this.memberShip = MemberShip.NORMAL;
        this.memberStatus = MemberStatus.ACTIVE;
    }

    /* 비즈니스 로직 */
    // 비밀번호 암호화한 것 저장
    public void convertEncryptionPassword (String encryptionPassword) {
        this.password = encryptionPassword;
    }

    // 로그인 시 lastLoginAt 변경
    public void changeLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    // 주소지 변경
    public void changeMemberAddress(Address newAddress) {
        this.address = newAddress;
    }

    // 폰번호 변경
    public void changePhoneNumber(String newPhone) {
        this.phone = newPhone;
    }

    // 비밀번호 변경
    public void changePassword (String encryptionPassword) {
        this.password = encryptionPassword;
    }

    // 멤버쉽 등급 변경 (NORMAL -> MEMBERSHIP)
    public void changeMemberShipToUpgrade() {
        this.memberShip = MemberShip.MEMBERSHIP;
    }

    // 멤버쉽 등급 변경 (MEMBERSHIP -> NORMAL)
    public void changeMemberShipToDowngrade() {
        this.memberShip = MemberShip.NORMAL;
    }

    // Member admin 변경
    public void changeToAdmin() {
        this.role = MemberRole.ADMIN;
    }

    // Member InActive 변경
    public void changeMemberStatus() {
        this.memberStatus = MemberStatus.INACTIVE;
    }

}
