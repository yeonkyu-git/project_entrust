package project.entrust.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Category;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
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
        memberService.changeToAdmin(member.getId());
    }

    @Test
    public void 카테고리생성() throws Exception {
        // given
        Member admin = callMember("dusrbpoiiij@naver.com");
        Long categoryId = categoryService.createCategory(admin.getId(), "상의");

        // when
        Category findCategory = categoryRepository.findById(categoryId).orElse(null);
        Assertions.assertThat(findCategory.getCategoryName()).isEqualTo("상의");
    }

    @Test
    public void 카테고리삭제() throws Exception {
        //given
        Member admin = callMember("dusrbpoiiij@naver.com");

        Long categoryId = categoryService.createCategory(admin.getId(), "상의");

        //when
        categoryService.deleteCategory(admin.getId(), categoryId);
        List<Category> result = categoryRepository.findAll();

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);

    }

    @Test
    public void 카테고리수정() throws Exception {
        //given
        Member admin = callMember("dusrbpoiiij@naver.com");

        Long categoryId = categoryService.createCategory(admin.getId(), "상의");

        //when
        categoryService.changeCategoryName(admin.getId(), categoryId, "하의");
        em.flush();
        em.clear();

        Category result = categoryRepository.findById(categoryId).orElse(null);

        //then
        Assertions.assertThat(result.getCategoryName()).isEqualTo("하의");

    }


    public Member callMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElse(null);
    }

}