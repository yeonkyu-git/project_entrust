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


        }
    }
}
