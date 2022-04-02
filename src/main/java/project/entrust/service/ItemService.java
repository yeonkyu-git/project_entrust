package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Item;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.repository.ItemRepository;
import project.entrust.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 아이템 등록 (제약사항 : Admin만 가능)
     */
    public Long createItem(Long enrollAdminId,
                           Long ownerId,
                           String itemName,
                           String description,
                           Long categoryId) {

        // 1. 등록하는 사람이 Admin 인지 확인
        Optional<Member> adminMember = memberRepository.findById(enrollAdminId);
        // Todo __ 회원이 Admin이 아닐 경우에 대한 오류 처리 필요
        if (adminMember.isEmpty() || adminMember.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 회원이 아닙니다.");
        }

        // 2. 아이템 주인 객체 및 카테고리 객체 조회 후 아이템 객체 생성
        Optional<Member> owner = memberRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new RuntimeException("옷 주인이 없어요!");
        }

        // Todo __ 카테고리 로직을 만들어야 함 !!
        return null;

    }
}
