package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.dto.member.RegisterMemberForm;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.entity.assistant.MemberShip;
import project.entrust.entity.assistant.MemberStatus;
import project.entrust.repository.MemberRepository;
import project.entrust.util.ConvertPassword;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * 1. 에러처리를 해야 한다.
 *  - 각 에러 처리마다 다른 유형의 화면 or API 제공하여야 한다.
 * 2. Controller에서 들어오는 것은 항상 Form이 들어올 수 있도록 한다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     */
    @Transactional
    public Member login(String email, String plainPassword) {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        // 1. 이메일 존재 여부 확인
        if (findMember.isEmpty()) {
            throw new IllegalStateException("이메일에 맞는 회원이 없습니다");
        }

        // 2. 비밀번호 일치 여부 확인
        Member member = findMember.orElse(null);
        if (!ConvertPassword.comparePassword(plainPassword, member.getPassword())) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }

        // 3. 비활성화 여부 확인
        if (member.getMemberStatus().equals(MemberStatus.INACTIVE)) {
            throw new IllegalStateException("탈퇴한 회원입니다.");
        }

        member.changeLastLoginAt();


        return member;
    }

    /**
     * 회원가입
     * 이메일을 기준으로 회원가입
     */
    @Transactional
    public Long save(RegisterMemberForm memberForm) {
        // 1. 이메일 중복 검사
        validateMemberDuplicate(memberForm.getEmail());

        // 2. 비밀번호 암호화
        String plainPassword = memberForm.getPassword();
        String password = ConvertPassword.encryptionPassword(plainPassword);

        // 3. 전화번호 정렬
        String phone = memberForm.getPhone1() + "-" + memberForm.getPhone2() + "-" + memberForm.getPhone3();

        // 3. Member 객체 생성
        Member member = Member.createMember(
                memberForm.getEmail(),
                password,
                memberForm.getUsername(),
                memberForm.getBirthday(),
                phone,
                memberForm.getAddress()
        );

        // 4. Member 저장
        memberRepository.save(member);

        return member.getId();
    }

    private void validateMemberDuplicate(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /**
     * 회원 비활성화 (탈퇴)
     */
    @Transactional
    public void convertInActive(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new IllegalStateException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        member.changeMemberStatus();
    }

    /**
     * 회원의 주소지 변경
     */
    @Transactional
    public void changeMemberAddress(Long memberId, Address newAddress) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        member.changeMemberAddress(newAddress);
    }

    /**
     * 회원의 폰번호 변경
     */
    @Transactional
    public void changeMemberPhone(Long memberId, String newPhone) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        member.changePhoneNumber(newPhone);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changeMemberPassword(Long memberId, String plainPassword) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        String encryptionPassword = ConvertPassword.encryptionPassword(plainPassword);
        member.changePassword(encryptionPassword);
    }

    /**
     * 회원의 멤버쉽 등급 변경 (NORMAL -> MEMBERSHIP)
     */
    @Transactional
    public void changeMemberShipUpgrade(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        if (member.getMemberShip().equals(MemberShip.MEMBERSHIP)) {
            throw new RuntimeException("이미 멤버쉽에 가입한 회원이에요!");
        }

        member.changeMemberShipToUpgrade();
    }

    /**
     * 회원의 멤버쉽 등급 변경 (MEMBERSHIP -> NORMAL)
     */
    @Transactional
    public void changeMemberShipDowngrade(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        if (member.getMemberShip().equals(MemberShip.NORMAL)) {
            throw new RuntimeException("당신은 NORMAL이에요!");
        }

        member.changeMemberShipToDowngrade();
    }

    /**
     * 회원 Admin 변경
     */
    @Transactional
    public void changeToAdmin(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            throw new RuntimeException("진행하는 도중 문제가 발생했어요!");
        }
        Member member = findMember.orElse(null);
        if (member.getRole().equals(MemberRole.ADMIN)) {
            throw new RuntimeException("당신은 이미 Admin이에요!");
        }

        member.changeToAdmin();
    }
}
