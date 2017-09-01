package net.scero.test.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import net.scero.test.controllers.IPAddressBasedAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IPAddressBasedAuthenticationProvider authenticationProvider;

    //  @Bean
    //  public UserDetailsService userDetailsService() {
    // InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    // manager.createUser(User.withUsername("user").password("password").roles("USER").build());
    // return manager;
    //  }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
            // antMatchers("/manage/**").hasRole("ACTUATOR").
            anyRequest().permitAll().and().formLogin().and().logout();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("password").roles("ACTUATOR");
        auth.authenticationProvider(authenticationProvider);
    }
}