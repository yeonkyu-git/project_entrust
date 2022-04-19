package project.entrust.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Category;
import project.entrust.entity.Item;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.repository.CategoryRepository;
import project.entrust.repository.ItemRepository;
import project.entrust.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {

        // Member Admin 생성
        Member admin = new Member(
                "admin@naver.com",
                "123456",
                "yeonkyu",
                LocalDate.of(1990, 9, 24),
                "01085472613",
                new Address("seoul", "sillim", "10020")
        );

        memberService.save(admin);
        memberService.changeToAdmin(admin.getId());

        // Member 회원 생성
        Member member = new Member(
                "dusrb@naver.com",
                "123456",
                "yeonkyu",
                LocalDate.of(1990, 9, 24),
                "01085472613",
                new Address("seoul", "sillim", "10020")
        );

        memberService.save(member);

        // 카테고리 생성
        Long categoryId = categoryService.createCategory(admin.getId(), "상의");

        // 아이템 생성
//        Item item = itemService.createItem(admin.getId(), member.getId(), "청바지", "산 지 1년된 옷", categoryId);


    }


    @Test
//    @Rollback(value = false)
    public void 아이템_생성() throws Exception {
        // given
        Member admin = callMember("admin@naver.com");
        Member member = callMember("dusrb@naver.com");
        Category category = callCategory("상의");

//        // when
//        Item item = itemService.createItem(admin.getId(), member.getId(), "청바지", "산 지 1년된 옷", category.getId());
//        Item findItem = itemRepository.findById(item.getId()).orElse(null);
//
//        // then
//        assertThat(findItem.getItemName()).isEqualTo("청바지");
//        assertThat(findItem.getDescription()).isEqualTo("산 지 1년된 옷");
//        assertThat(findItem.getCategory()).isEqualTo(category);
    }

    @Test
    public void 아이템생성실패_Admin아님() throws Exception {
        Member member = callMember("dusrb@naver.com");
        Category category = callCategory("상의");

//        assertThrows(RuntimeException.class, () -> {
//                itemService.createItem(member.getId(), member.getId(), "청바지", "산 지 1년된 옷", category.getId());
//                }
//        );
    }

    @Test
    public void 아이템생성실패_카테고리_없음() throws Exception {
        Member admin = callMember("admin@naver.com");
//        Category category = callCategory("상의");

//        assertThrows(RuntimeException.class, () -> {
//                itemService.createItem(admin.getId(), admin.getId(), "청바지", "산 지 1년된 옷", 1L);
//                }
//        );
    }

    @Test
    public void 아이템삭제() throws Exception {
        Member admin = callMember("admin@naver.com");
        Member member = callMember("dusrb@naver.com");
        Item item = itemRepository.findByOwner(member).get(0);

        itemService.deleteItem(admin.getId(), item.getId());

        List<Item> all = itemRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void 아이템삭제_실패_Admin아님() throws Exception {
        //given
        Member member = callMember("dusrb@naver.com");
        Item item = itemRepository.findByOwner(member).get(0);

        //when
        assertThrows(RuntimeException.class, () -> {
                itemService.deleteItem(member.getId(), item.getId());
                }
        );

    }

    @Test
    public void 아이템삭제_실패_아이템조회_안됨() throws Exception {
        //given
        Member admin = callMember("admin@naver.com");
        Member member = callMember("dusrb@naver.com");
        Item item = itemRepository.findByOwner(member).get(0);

        //when
        assertThrows(RuntimeException.class, () -> {
                itemService.deleteItem(admin.getId(), 1L);
                }
        );
    }

    @Test
    public void 아이템_수정() throws Exception {
        //given
        Member member = callMember("dusrb@naver.com");
        Item item = itemRepository.findByOwner(member).get(0);

        //when
        itemService.updateItem(member.getId(), item.getId(), "흰바지", "하이루");
        Item findItem = itemRepository.findById(item.getId()).orElse(null);

        //then
        assertThat(findItem.getItemName()).isEqualTo("흰바지");
        assertThat(findItem.getDescription()).isEqualTo("하이루");

    }



    public Member callMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElse(null);
    }

    public Category callCategory(String categoryName) {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        return category.orElse(null);
    }

}