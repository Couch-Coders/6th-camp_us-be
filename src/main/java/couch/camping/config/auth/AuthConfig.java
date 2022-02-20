package couch.camping.config.auth;


import com.google.firebase.auth.FirebaseAuth;
import couch.camping.domain.member.service.MemberService;
import couch.camping.filter.JwtFilter;
import couch.camping.filter.MockJwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthConfig {

    private final MemberService userService;

    private final FirebaseAuth firebaseAuth;

    @Bean
    @Profile("local")
    public AuthFilterContainer mockAuthFilter() {
        log.info("Initializing local AuthFilter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(new MockJwtFilter(userService));
        return authFilterContainer;
    }

    @Bean
    @Profile("prod")
    public AuthFilterContainer firebaseAuthFilter() {
        log.info("Initializing Firebase AuthFilter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(new JwtFilter(userService, firebaseAuth));
        return authFilterContainer;
    }
}
