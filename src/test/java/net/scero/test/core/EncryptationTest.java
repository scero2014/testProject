package net.scero.test.core;

import static org.junit.Assert.assertTrue;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;

public class EncryptationTest {
    @Test
    public void test() {
        String password = "holaLola";
        
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        System.out.println(encryptedPassword);
        assertTrue(passwordEncryptor.checkPassword(password, encryptedPassword));
        
        // Para usar StrongTextEncryptor hay que instalar librerías adicionales
        String myEncryptionPassword = "hfsjhsdiojvlkmewli";
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(myEncryptionPassword);
        
        String textoGuapo = "hola peque";
        String myEncryptedText = textEncryptor.encrypt(textoGuapo);
        System.out.println(myEncryptedText);
        
        assertTrue(textoGuapo.equals(textEncryptor.decrypt(myEncryptedText)));
    }
}
