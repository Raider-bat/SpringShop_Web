package kz.gexa.spring.shop.config;


import kz.gexa.spring.shop.config.handler.MyAuthenticationSuccessHandler;
import kz.gexa.spring.shop.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private final
    UserService userService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(MyAuthenticationSuccessHandler myAuthenticationSuccessHandler,
                             UserService userService,
                             PasswordEncoder passwordEncoder) {
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests()
                    .antMatchers("/" , "/registration", "/main/**").permitAll()
                    .antMatchers("/dashboard").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(myAuthenticationSuccessHandler)
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/resources/**", "/static/**", "/css/**",
                "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
                "/webjars/**", "/swagger-resources/**", "/static/bootstrap/css/**",
                "/static/bootstrap/js/**","/bootstrap/css/**", "/bootstrap/js/**"
        );
    }
}