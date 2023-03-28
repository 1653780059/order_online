package com.example.order_online.config;


import com.example.order_online.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author 16537
 * @Classname SecurityConfig
 * @Description SpringSecurity配置
 * @Version 1.0.0
 * @Date 2022/12/21 17:29
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Resource
    private LoginFilter loginFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //跨域 预检查  需要放行所有OPTIONS操作
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                //.antMatchers("/**").permitAll()
                .antMatchers("/sys/login","/sys/verification","/user/checkUserName","/sys/uploadUserImg","/user/register").anonymous()
                .antMatchers("/es/**/*","/sys/logout","/file/upload","/sys/email/verification","/socket/**").permitAll()
                .antMatchers("/wx/native/notify","/wx/refunds/notify").anonymous()
                //放行静态资源和swagger资源
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
