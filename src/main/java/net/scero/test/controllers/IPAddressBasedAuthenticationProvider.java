package net.scero.test.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service 
public class IPAddressBasedAuthenticationProvider implements AuthenticationProvider {
//     @Autowired
//     private HttpServletRequest request;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        if ("user".equals(name) && "password".equals(password)){
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("Ok ADMIN");
        }

        WebAuthenticationDetails wad = (WebAuthenticationDetails) authentication.getDetails();
        String ip = wad.getRemoteAddress();

        if ("0:0:0:0:0:0:0:1".equals(ip)){
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_IP"));
        }
        
        return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}