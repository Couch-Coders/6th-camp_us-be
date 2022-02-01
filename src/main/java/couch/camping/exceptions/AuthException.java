package couch.camping.exceptions;

public class AuthException extends Exception {
    private static final long serialVersionUID = 1L;
    private String code;

    /**
     * enum 에 등록해야함
     */
    public static final String CODE_INVALID_TOKEN = "invalid_token";
    public static final String CODE_USER_NOT_FOUND = "user_not_found";

    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }  
}
