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
        Optional<Member> admin = memberRepository.findById(adminId);

        if (admin.isEmpty() || admin.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 이 아닌거 같아요!");
        }

        if (validateCategoryNameDuplicate(categoryName)) {
            Category category = new Category(categoryName);
            categoryRepository.save(category);
            return category.getId();
        } else{
            throw new RuntimeException("똑같은 카테고리 이름이 있어요!");
        }
    }

    private boolean validateCategoryNameDuplicate(String categoryName) {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        return category.isEmpty();
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void changeCategoryName(Long adminId, Long categoryId, String newCategoryName) {
        Optional<Member> admin = memberRepository.findById(adminId);
        if (admin.isEmpty() || admin.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 이 아닌거 같아요!");
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("요청하신 카테고리가 없어요!");
        }

        category.orElse(null).changeCategoryName(newCategoryName);

    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long adminId, Long categoryId) {
        Optional<Member> admin = memberRepository.findById(adminId);
        if (admin.isEmpty() || admin.orElse(null).getRole().equals(MemberRole.NORMAL)) {
            throw new RuntimeException("Admin 이 아닌거 같아요!");
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("요청하신 카테고리가 없어요!");
        }

        categoryRepository.delete(category.orElse(null));
    }


}

