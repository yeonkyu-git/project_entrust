package project.entrust;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.entrust.entity.Item;
import project.entrust.entity.Member;
import project.entrust.entity.assistant.Address;
import project.entrust.service.CategoryService;
import project.entrust.service.ItemService;
import project.entrust.service.MemberService;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

//@Component
@RequiredArgsConstructor
@Slf4j
public class TestInit {

    private final InitService initService;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        initService.dbInit();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {
        private final MemberService memberService;
        private final CategoryService categoryService;
        private final ItemService itemService;

        public void dbInit() {

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
            Item item = itemService.createItem(admin.getId(), member.getId(), "청바지", "산 지 1년된 옷", categoryId);



        }
    }
}
