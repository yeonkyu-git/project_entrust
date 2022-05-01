package project.entrust.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.entrust.dto.member.RegisterMemberForm;
import project.entrust.entity.Category;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void beforeMethod() {
        RegisterMemberForm form = new RegisterMemberForm(
                "admin@admin.com",
                "1234",
                "5555",
                "kim",
                LocalDate.now(),
                "010",
                "8547",
                "2613",
                new Address("서울", "신림로", "123123"));
        Long memberId = memberService.save(form);
        memberService.changeToAdmin(memberId);
    }


    @Test
    @DisplayName("카테고리 등록")
    public void enroll_Category() throws Exception {
        //given
        Member admin = memberRepository.findByEmail("admin@admin.com").orElseThrow(EntityNotFoundException::new);
        String categoryName = "상의";

        //when
        Long categoryId = categoryService.createCategory(admin.getId(), categoryName);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(category.getCategoryName()).isEqualTo("상의");
    }

    @Test
    @DisplayName("카테고리 변경")
    public void change_Category() throws Exception {
        //given
        Member admin = memberRepository.findByEmail("admin@admin.com").orElseThrow(EntityNotFoundException::new);
        String categoryName = "상의";

        //when
        Long categoryId = categoryService.createCategory(admin.getId(), categoryName);
        categoryService.changeCategoryName(admin.getId(), categoryId, "하의");
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(category.getCategoryName()).isEqualTo("하의");
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void delete_Category() throws Exception {
        //given
        Member admin = memberRepository.findByEmail("admin@admin.com").orElseThrow(EntityNotFoundException::new);
        String categoryName = "상의";

        //when
        Long categoryId = categoryService.createCategory(admin.getId(), categoryName);
        categoryService.deleteCategory(admin.getId(), categoryId);

        Optional<Category> category = categoryRepository.findById(categoryId);

        //then
        Assertions.assertThat(category.orElse(null)).isNull();
    }
}