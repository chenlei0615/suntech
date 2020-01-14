package com.suntech.feo.config;

import com.suntech.feo.security.JwtAuthenticationEntryPoint;
import com.suntech.feo.security.JwtAuthenticationTokenFilter;
import com.suntech.feo.security.MyAccessDeniedHandler;
import com.suntech.feo.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config
 * @Description : Spring Security 的配置类
 * @Author : chenlei
 * @Create Date : 2020年01月03日 10:53
 * ------------ -------------- ---------------------------------
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private  JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomUserDetailsService CustomUserDetailsService;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private  JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 需要放行的URL
     */
    private static final String[] AUTH_WHITELIST = {
            // -- register url
            "/users/signup",
            "/users/addTask",
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/doc.html",
            // system open
            "/open/**",
            "/user/login",
             "/user/register"

    };

    /**
     * 验证
     * @param unauthorizedHandler 认证失败处理类
     * @param accessDeniedHandler 权限不足处理类
     * @param customUserDetailsService 实现了DetailsService接口，用来做登陆验证
     * @param authenticationTokenFilter  token过滤器来验证token有效性
     */
    @Autowired
    public void WebSecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                                  @Qualifier("MyAccessDeniedHandler") MyAccessDeniedHandler accessDeniedHandler,
                             @Qualifier("CustomUserDetailsService") CustomUserDetailsService customUserDetailsService,
                             JwtAuthenticationTokenFilter authenticationTokenFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.CustomUserDetailsService = customUserDetailsService;
        this.myAccessDeniedHandler = accessDeniedHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService 获取user对象
                .userDetailsService(this.CustomUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }


    /**
     * 装载BCrypt密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
//                .antMatchers("/user/**").hasAnyRole("ADMIN","USER")
                .anyRequest().permitAll()       // 允许所有请求通过
                .and()
                // 配置被拦截时的处理
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler).and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter 在 Spring Security 开始判断本次会话是否有权限时的前一瞬间 通过添加过滤器将
        // token 解析，将用户所有的权限写入本次会话
        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(CustomUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
