package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Category;
import project.entrust.entity.Item;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.ItemRepository;
import project.entrust.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

/**
 * 1. 개발 필요 부분
 * 아이템 조회에 대한 부분은 Querydsl 로 페이징 처리 및 기타 동적 쿼리 지원 가능하여야 한다.
 */





@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 아이템 등록 (제약사항 : Admin만 가능)
     */
    @Transactional
    public Item createItem(Long enrollAdminId,
                           Long ownerId,
                           String itemName,
                           String description,
                           Long categoryId) {

        // 1. 등록하는 사람이 Admin 인지 확인
        Optional<Member> adminMemberOptional = memberRepository.findById(enrollAdminId);
        // Todo __ 회원이 Admin이 아닐 경우에 대한 오류 처리 필요
        if (adminMemberOptional.isEmpty() || adminMemberOptional.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 회원이 아닙니다.");
        }

        // 2. 아이템 주인 객체 조회하기
        Optional<Member> ownerOptional = memberRepository.findById(ownerId);
        if (ownerOptional.isEmpty()) {
            throw new RuntimeException("옷 주인이 없어요!");
        }

        // 3. 카테고리 객체 조회하기
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new RuntimeException("카테고리가 없어요!");
        }

        // 4. Item 객체 생성 및 저장
        Item item = new Item(itemName, description, ownerOptional.orElse(null), categoryOptional.orElse(null));
        itemRepository.save(item);

        // 5. Item Image 생성
        return item;
    }


    /**
     * 아이템 수정 (Admin이 아닌 옷의 주인이 아이템에 대한 수정 개시)
     */
    @Transactional
    public void updateItem(Long memberId,
                           Long itemId,
                           String itemName,
                           String description) {
        // 1. 회원 조회
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        // Todo __ 오류 처리 필요
        if (memberOptional.isEmpty()) {
            throw new RuntimeException("회원이 없습니다.");
        }

        // 2. 아이템 조회
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new RuntimeException("아이템이 없습니다.");
        }

        // 3. 아이템 추출 및 업데이트
        Item item = itemOptional.orElse(null);  // 기본키인 itemId 로 조회했기 때문에 값이 하나라는 건 보장됨
        item.updateItemNameAndDescription(itemName, description);
    }


    /**
     * 아이템 삭제 (제약사항 : Admin만 가능)
     */
    @Transactional
    public void deleteItem(Long enrollAdminId, Long itemId) {
        // 1. 삭제하는 사람이 Admin 인지 확인
        Optional<Member> adminMemberOptional = memberRepository.findById(enrollAdminId);
        // Todo __ 회원이 Admin이 아닐 경우에 대한 오류 처리 필요
        if (adminMemberOptional.isEmpty() || adminMemberOptional.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 회원이 아닙니다.");
        }

        // 2. 아이템 조회
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new RuntimeException("아이템이 비정상입니다.");
        }

        itemRepository.delete(itemOptional.orElse(null));
    }
}
