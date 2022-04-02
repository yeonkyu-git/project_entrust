package project.entrust.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class ConvertPassword {

    private final static String SALT_CODE = "projectDemo";

    /**
     * 암호화할 때 salt 값 난수로 반환
     * 각 회원이 로그인 할 때 이 난수를 어떻게 가져오냐가 문제임.
     * 우선전으로 이 난수를 고정해버리고 프로젝트 진행
     */
    public static String Salt() {
        String salt = "";

        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            salt = new String(Base64.getEncoder().encode(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return salt;
    }

    /**
     * 비밀번호 암호화
     */
    public static String encryptionPassword(String password) {
        String salt = SALT_CODE+password;
        String hex = null;
        try {
            MessageDigest msg = MessageDigest.getInstance("SHA-512");
            msg.update(salt.getBytes());
            hex = String.format("%128x", new BigInteger(1, msg.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hex;
    }

    public static boolean comparePassword(String plainPassword, String password) {
        String inputPassword = encryptionPassword(plainPassword);
        if (inputPassword.equals(password)) {
            return true;
        }
        return false;
    }


}
