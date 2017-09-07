package net.scero.test.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenSecurityTest {
    @Test
    public void test() {
        TokenSecurity ts = new TokenSecurity();
        String token = ts.createJWT(new User("scero", 20, "ADMIN"), 100);
        User user = ts.authenticate(token);
        System.out.println(user.toString());
        assertTrue("scero".equals(user.getUser()));
    }
}
