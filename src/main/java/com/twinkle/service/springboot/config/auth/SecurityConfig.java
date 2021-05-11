package com.twinkle.service.springboot.config.auth;

import com.twinkle.service.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정을 활성화 시켜줌.
public class SecurityConfig extends
        WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .headers().frameOptions().disable()// h2-console 화면을 사용하기 위해 해당 옵션들을 disable.
        .and()
            // URL별 권한 관리를 설정하는 옵션의 시작점.
            // authorizeRequests가 선언되어야만 antMatchers 옵션 사용 가능.
            .authorizeRequests()
            // 권한 관리 대상을 지정하는 옵션. URL, HTTP 메소드별로 관리 가능.
            .antMatchers("/", "/css/**", "/images/**",
                "/js/**", "/h2-console/**", "/profile").permitAll()
            .antMatchers("/api/v1/**").hasRole(Role.USER.name())
            .anyRequest().authenticated() // 나머지 URL들은 모두 인증된 사용자들에게만 허용.
        .and()
            .logout()
                .logoutSuccessUrl("/")
        .and()
            .oauth2Login()// OAuth2 로그인 성공 이후 사용자 정보를 가져올때의 설정들을 담당.
                .userInfoEndpoint()
                    .userService(customOAuth2UserService);
    }
}
