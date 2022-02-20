package couch.camping.config.auth;

import org.springframework.web.filter.OncePerRequestFilter;

public class AuthFilterContainer {

    private OncePerRequestFilter authFilter;

    public void setAuthFilter(OncePerRequestFilter authFilter) {
        this.authFilter = authFilter;
    }

    public OncePerRequestFilter getFilter() {
        return authFilter;
    }
}
