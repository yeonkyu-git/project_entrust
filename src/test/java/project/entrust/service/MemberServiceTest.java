package project.entrust.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.MemberShip;
import project.entrust.repository.MemberRepository;
import project.entrust.util.ConvertPassword;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback(value = true)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void init() {
        Member member = new Member(
                "dusrbpoiiij@naver.com",
                "123456",
                "yeonkyu",
                LocalDate.of(1990, 9, 24),
                "01085472613",
                new Address("seoul", "sillim", "10020")
        );

        memberService.save(member);
    }


    @Test
    public void 회원가입() throws Exception {
        Member member = new Member(
                "dusrbpoiiij@daum.com",
                "123456",
                "yeonkyu",
                LocalDate.of(1990, 9, 24),
                "01085472613",
                new Address("seoul", "sillim", "10020")
        );
        memberService.save(member);
        List<Member> findMember = memberRepository.findAll();
        assertThat(findMember.size()).isEqualTo(2);
        assertThat(findMember.get(1).getPassword()).isEqualTo(ConvertPassword.encryptionPassword("123456"));

        System.out.println("findMember = " + findMember);
    }

    @Test
    public void 로그인() throws Exception {
        //given
        // 위에 beforeEach에 있음

        //when
        Member loginMember = memberService.login("dusrbpoiiij@naver.com", "123456");

        //then
        assertThat(loginMember.getUsername()).isEqualTo("yeonkyu");
        System.out.println("loginMember = " + loginMember);
    }

    @Test
    public void 로그인실패() throws Exception {
        //given
        // 위에 beforeEach에 있음

        //when
        // 이메일이 다를 경우
        assertThrows(RuntimeException.class, () -> {
                memberService.login("dusrbpoiii@naver.com", "123456");
            }
        );

        // 비밀번호가 다를 경우
        assertThrows(RuntimeException.class, () -> {
                memberService.login("dusrbpoiiij@naver.com", "12345");
            }
        );
    }

    @Test
    public void 주소지변경() throws Exception {
        //given
        Address newAddress = new Address("busan", "haundae", "123456");
        Member member = callMember("dusrbpoiiij@naver.com");

        //when
        memberService.changeMemberAddress(member.getId(), newAddress);
        em.flush();
        em.clear();

        //then
        Member findMember = callMember("dusrbpoiiij@naver.com");
        assertThat(findMember.getAddress().getCity()).isEqualTo("busan");
    }

    @Test
    public void 폰번호변경() throws Exception {
        //given
        Member member = callMember("dusrbpoiiij@naver.com");

        //when
        memberService.changeMemberPhone(member.getId(), "01085472614");
        em.flush();
        em.clear();

        //then
        Member findMember = callMember("dusrbpoiiij@naver.com");
        assertThat(findMember.getPhone()).isEqualTo("01085472614");
    }

    @Test
    public void 비밀번호변경() throws Exception {
        //given
        Member member = callMember("dusrbpoiiij@naver.com");

        //when
        memberService.changeMemberPassword(member.getId(), "123");
        em.flush();
        em.clear();

        //then
        Member findMember = callMember("dusrbpoiiij@naver.com");
        assertThat(findMember.getPassword()).isEqualTo(ConvertPassword.encryptionPassword("123"));
    }

    @Test
    public void 멤버쉽등급변경_UPGRADE() throws Exception {
        //given
        Member member = callMember("dusrbpoiiij@naver.com");

        //when
        memberService.changeMemberShipUpgrade(member.getId());
        em.flush();
        em.clear();

        //then
        Member findMember = callMember("dusrbpoiiij@naver.com");
        assertThat(findMember.getMemberShip()).isEqualTo(MemberShip.MEMBERSHIP);
    }

    @Test
    public void 멤버쉽등급변경_DOWNGRADE() throws Exception {
        //given
        Member member = callMember("dusrbpoiiij@naver.com");
        memberService.changeMemberShipUpgrade(member.getId());

        //when
        memberService.changeMemberShipDowngrade(member.getId());
        em.flush();
        em.clear();

        //then
        Member findMember = callMember("dusrbpoiiij@naver.com");
        assertThat(findMember.getMemberShip()).isEqualTo(MemberShip.NORMAL);
    }


    // loginMember 불러오기
    public Member callMember(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.orElse(null);
    }
}

