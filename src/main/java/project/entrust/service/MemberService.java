package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.entity.assistant.MemberShip;
import project.entrust.repository.MemberRepository;
import project.entrust.util.ConvertPassword;

import java.util.Optional;

/**
 * 1. 에러처리를 해야 한다.
 *  - 각 에러 처리마다 다른 유형의 화면 or API 제공하여야 한다.
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

        // Todo __ 에러 처리 필요 (회원 중 해당하는 이메일이 없을 경우)
        if (findMember.isEmpty()) {
            throw new RuntimeException("이메일에 맞는 회원이 없습니다");
        }

        Member member = findMember.orElse(null);
        if (!ConvertPassword.comparePassword(plainPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 틀립니다.");
        }
        member.changeLastLoginAt();

        // Todo __ Controller 에서 회원의 Id, 이름을 보관하다.
        return member;
    }

    /**
     * 회원가입
     * 이메일을 기준으로 회원가입
     */
    @Transactional
    public Long save(Member member) {
        if (validateMemberDuplicate(member.getEmail())) {
            // 비밀번호 암호화
            String encryptionPassword = ConvertPassword.encryptionPassword(member.getPassword());
            member.convertEncryptionPassword(encryptionPassword);

            // 회원 저장
            memberRepository.save(member);
        }
        return member.getId();
    }

    private boolean validateMemberDuplicate(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.isEmpty();
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
