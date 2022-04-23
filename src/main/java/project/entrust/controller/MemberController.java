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
import project.entrust.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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

        // 1. 비밀번호 일치 여부 확인
        String originPassword = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        if (!originPassword.equals(confirmPassword)) {
            bindingResult.reject("mismatchPassword", "비밀번호가 일치하지 않습니다.");
        }

        // 2. Validation
        if (bindingResult.hasErrors()) {
            log.info("Validation Error 발생");
            return "member/register";
        }

        memberService.save(form);

        return "/";
    }
}
