package project.entrust.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ConvertPasswordTest {


    @Test
    public void EncryptionPassword() throws Exception {
        String password = "123456";
        String plainPassword = "123456";
        String encryptionPassword = ConvertPassword.encryptionPassword(password);


        boolean result = ConvertPassword.comparePassword(plainPassword, encryptionPassword);

        Assertions.assertThat(result).isTrue();

    }

}