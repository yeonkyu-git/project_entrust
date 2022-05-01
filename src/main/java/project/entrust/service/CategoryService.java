package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Category;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.MemberRole;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * 1. 에러처리를 해야 한다.
 *  - 각 에러 처리마다 다른 유형의 화면 or API 제공하여야 한다.
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;


    /**
     * 카테고리 등록
     */
    @Transactional
    public Long createCategory(Long adminId, String categoryName) {
        validateAdminMember(adminId);

        // 1. 카테고리 이름 중복 확인
        validateCategoryNameDuplicate(categoryName);

        // 2. 카테고리 저장
        Category category = Category.createCategory(categoryName);
        categoryRepository.save(category);

        return category.getId();
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void changeCategoryName(Long adminId, Long categoryId, String newCategoryName) {
        validateAdminMember(adminId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        category.changeCategoryName(newCategoryName);
    }


    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long adminId, Long categoryId) {
        validateAdminMember(adminId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        categoryRepository.delete(category);
    }



    private void validateAdminMember(Long adminId) {
        Optional<Member> OptionalAdmin = memberRepository.findById(adminId);

        // 1. 찾고자 하는 Member가 있는지 확인
        if (OptionalAdmin.isEmpty()) {
            throw new IllegalStateException("카테고리를 등록하려는 유저가 없습니다.");
        }

        Member admin = OptionalAdmin.orElse(null);

        // 2. Admin 인지 확인
        if (admin.getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 이 아닌거 같아요!");
        }
    }

    private void validateCategoryNameDuplicate(String categoryName) {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        if (category.isPresent()) {
            throw new IllegalStateException("중복된 카테고리 입니다.");
        }
    }
}

