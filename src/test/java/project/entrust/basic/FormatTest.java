package project.entrust.basic;

import org.junit.jupiter.api.Test;
import project.entrust.dto.member.RegisterMemberForm;

import java.time.LocalDate;

public class FormatTest {


    @Test
    public void DataFormat() throws Exception {
        RegisterMemberForm form = new RegisterMemberForm();
        form.setBirthday(LocalDate.now());

        System.out.println(form.getBirthday());
    }
}
