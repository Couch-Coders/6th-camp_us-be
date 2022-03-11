package couch.camping.config;

import couch.camping.config.auth.AuthFilterContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthFilterContainer authFilterContainer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제
                .csrf().disable() // csrf 보안 토큰 disable 처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // 요청에 대한 권한 지정
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight Request 허용해주기
                .anyRequest().authenticated() // 모든 요청이 인증되어야한다.
                .and()
                .addFilterBefore(authFilterContainer.getFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //인증 예외 URL 설정
        web.ignoring()
                .antMatchers(HttpMethod.GET ,"/test")
                .antMatchers(HttpMethod.POST ,"/members/local")//로컬용 회원가입
                .antMatchers(HttpMethod.POST, "/members")//배포용 회원가입
                .antMatchers(HttpMethod.POST, "/camps")
                .antMatchers(HttpMethod.GET ,"/camps/**")
                .antMatchers(HttpMethod.GET, "/reviews/**")
                .antMatchers(HttpMethod.GET, "/posts/**")
                .antMatchers("/css/**")
                .antMatchers("/static/**")
                .antMatchers("/js/**")
                .antMatchers("/img/**")
                .antMatchers("/fonts/**")
                .antMatchers("/vendor/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/pages/**")
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**");
    }
}
