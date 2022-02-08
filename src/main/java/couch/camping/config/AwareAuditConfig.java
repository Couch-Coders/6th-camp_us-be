package couch.camping.config;

import couch.camping.domain.member.entity.Member;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AwareAuditConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member;
        try {
             member = (Member) authentication.getPrincipal();
        } catch (NullPointerException e) {
            return null;
        }
        return Optional.of(member.getNickname());
    }
}