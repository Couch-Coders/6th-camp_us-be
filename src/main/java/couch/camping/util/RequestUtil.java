package couch.camping.util;

import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtil {

    // 헤더값 검증
    public static String getAuthorizationToken(String header) {
        log.info("incoming Authorization header: " + header);
        //헤더값에 Authorization 값이 없거나 유효하지 않은 경우
        if(header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_AUTHORIZATION);
        }

        // parts[0] : bearer, parts[1] : token
        String[] parts = header.split(" ");
        if(parts.length != 2) {
            throw new CustomException(ErrorCode.INVALID_AUTHORIZATION);
        }

        //Token return
        return parts[1];
    }
}
