package project.entrust.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.entrust.dto.member.RegisterMemberForm;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    /**
     * 회원 가입 화면 호출
     */
    @GetMapping("/register")
    public String registerView(@ModelAttribute RegisterMemberForm form) {
        log.info("RegisterView");
        return "member/register";
    }

    /**
     * 회원 가입 로직
     */
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterMemberForm form,
                           BindingResult bindingResult) {
        log.info("RegisterView");

        // 1. 각 프로퍼티 Validation 점검
        if (bindingResult.hasErrors()) {
            log.info("Validation Error 발생");
            return "member/register";
        }

        // 2. 비밀번호 일치 여부 확인
        String originPassword = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        if (!originPassword.equals(confirmPassword)) {

        }


    }
}
