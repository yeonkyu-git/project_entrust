package project.entrust.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.entrust.dto.member.RegisterMemberForm;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.entity.assistant.MemberShip;
import project.entrust.entity.assistant.MemberStatus;
import project.entrust.repository.MemberRepository;
import project.entrust.util.ConvertPassword;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    public RegisterMemberForm init() {
        return new RegisterMemberForm(
                "dusrbpoiiij@naver.com",
                "1234",
                "5555",
                "kim",
                LocalDate.now(),
                "010",
                "8547",
                "2613",
                new Address("서울", "신림로", "123123"));

    }

    @Test
    @DisplayName("회원가입")
    public void join() throws Exception {
        //given
        RegisterMemberForm form = init();

        //when
        Long memberId = memberService.save(form);
        Member findMember = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);

        //then
        assertThat(findMember.getEmail()).isEqualTo("dusrbpoiiij@naver.com");
        assertThat(findMember.getUsername()).isEqualTo("kim");
        assertThat(findMember.getPhone()).isEqualTo("010-8547-2613");
        assertThat(findMember.getRole()).isEqualTo(MemberRole.NORMAL);
        assertThat(findMember.getMemberShip()).isEqualTo(MemberShip.NORMAL);
        assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);

        System.out.println("findMember = " + findMember);
    }

    @Test
    @DisplayName("중복회원 방지")
    public void duplicateJoin() {
        // given
        RegisterMemberForm form1 = init();
        RegisterMemberForm form2 = init();

        // when
        memberService.save(form1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.save(form2);
        });

        // then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 회원입니다.");
    }

    @Test
    @DisplayName("로그인")
    public void login() throws Exception {
        //given
        RegisterMemberForm form = init();
        memberService.save(form);

        //when
        Member loginMember = memberService.login("dusrbpoiiij@naver.com", "1234");

        //then
        assertThat(loginMember.getEmail()).isEqualTo("dusrbpoiiij@naver.com");
        assertThat(loginMember.getUsername()).isEqualTo("kim");
        assertThat(loginMember.getPhone()).isEqualTo("010-8547-2613");
        assertThat(loginMember.getRole()).isEqualTo(MemberRole.NORMAL);
        assertThat(loginMember.getMemberShip()).isEqualTo(MemberShip.NORMAL);
        assertThat(loginMember.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);

    }

    @Test
    @DisplayName("로그인 실패__이메일 없음")
    public void loginFail_WrongEmail() throws Exception {
        //given
        RegisterMemberForm form = init();
        memberService.save(form);

        //when
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.login("test@naver.com", "1234");
        });


        //then
        assertThat(e.getMessage()).isEqualTo("이메일에 맞는 회원이 없습니다");
    }

    @Test
    @DisplayName("로그인 실패__비밀번호 불일치")
    public void loginFail_WrongPassword() throws Exception {
        //given
        RegisterMemberForm form = init();
        memberService.save(form);

        //when
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.login("dusrbpoiiij@naver.com", "123");
        });

        //then
        assertThat(e.getMessage()).isEqualTo("비밀번호가 틀립니다.");
    }

    @Test
    @DisplayName("회원 비활성화")
    public void inActive_Member() throws Exception {
        //given
        RegisterMemberForm form = init();
        Long memberId = memberService.save(form);

        //when
        memberService.convertInActive(memberId);

        //then
        Member findMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.INACTIVE);

    }

    @Test
    @DisplayName("회원 비활성화된 상태에서 로그인 하기 ")
    public void inActive_Member_login() throws Exception {
        //given
        RegisterMemberForm form = init();
        Long memberId = memberService.save(form);

        //when
        memberService.convertInActive(memberId);
        Member findMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.login("dusrbpoiiij@naver.com", "1234");
        });

        //then
        assertThat(e.getMessage()).isEqualTo("탈퇴한 회원입니다.");

    }

}