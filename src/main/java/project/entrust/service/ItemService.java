package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.entrust.dto.item.CreateItemForm;
import project.entrust.dto.item.UpdateItemForm;
import project.entrust.entity.Category;
import project.entrust.entity.Item;
import project.entrust.entity.ItemImage;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.ItemRepository;
import project.entrust.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
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
    private final ItemImageService itemImageService;

    /**
     * 아이템 등록 (제약사항 : Admin만 가능)
     */
    @Transactional
    public Long createItem(CreateItemForm createItemForm, List<MultipartFile> multipartFiles) throws IOException {

        // 1. 등록하는 사람이 Admin 인지 확인
        Optional<Member> adminMemberOptional = memberRepository.findById(createItemForm.getAdminId());
        // Todo __ 회원이 Admin이 아닐 경우에 대한 오류 처리 필요
        if (adminMemberOptional.isEmpty() || adminMemberOptional.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 회원이 아닙니다.");
        }

        // 2. 아이템 주인 객체 조회하기
        Optional<Member> ownerOptional = memberRepository.findById(createItemForm.getOwnerId());
        if (ownerOptional.isEmpty()) {
            throw new RuntimeException("옷 주인이 없어요!");
        }

        // 3. 카테고리 객체 조회하기
        Optional<Category> categoryOptional = categoryRepository.findById(createItemForm.getCategoryId());
        if (categoryOptional.isEmpty()) {
            throw new RuntimeException("카테고리가 없어요!");
        }

        // 4. 이미지 저장
        List<ItemImage> itemImages = itemImageService.saveItemImage(multipartFiles);

        // 5. 아이템 객체 생성 수 저장
        Item item = Item.createItem(createItemForm.getItemName(),
                createItemForm.getDescription(),
                ownerOptional.orElse(null),
                categoryOptional.orElse(null),
                itemImages);
        itemRepository.save(item);
        return item.getId();
    }


    /**
     * 아이템 수정 (Admin이 아닌 옷의 주인이 아이템에 대한 수정 개시)
     * (Admin는 아이템 수정 권한이 없다.)
     */
    @Transactional
    public void updateItem(UpdateItemForm updateItemForm) {
        // 1. 회원 조회
        Member member = memberRepository.findById(updateItemForm.getMemberId()).orElseThrow(EntityNotFoundException::new);

        // 2. 아이템 조회
        Item item = itemRepository.findById(updateItemForm.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 3. 아이템 업데이트
        item.updateItemNameAndDescription(updateItemForm.getItemName(), updateItemForm.getDescription());
    }


    /**
     * 아이템 삭제 (제약사항 : Admin만 가능)
     */
    @Transactional
    public void deleteItem(Long enrollAdminId, Long itemId) {
        // 1. 삭제하는 사람이 Admin 인지 확인
        Member member = memberRepository.findById(enrollAdminId).orElseThrow(EntityNotFoundException::new);

        // Todo __ 회원이 Admin이 아닐 경우에 대한 오류 처리 필요
        if (member.getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 회원이 아닙니다.");
        }

        // 2. 아이템 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 3. 서버에 있는 아이템 이미지 모두 삭제
        itemImageService.deleteAllItemImage(item.getItemImages());

        // 4. 아이템 객체 및 아이템 이미지 객체 모두 삭제
        itemRepository.delete(item);
    }

    /**
     * 아이템 조회
     */
}
