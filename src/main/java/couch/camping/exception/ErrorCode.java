package couch.camping.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    //공통 예외
    BAD_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BAD_REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, "검증에 실패하였습니다."),

    //회원 예외
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "해당 요청은 로그인이 필요합니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    EXIST_MEMBER(HttpStatus.BAD_REQUEST, "이미 등록된 유저입니다."),

    //인증 인가 예외
    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "해당 요청에 권한이 없습니다."),
    INVALID_AUTHORIZATION(HttpStatus.BAD_REQUEST, "인증 정보가 부정확합니다."),

    //캠핑장 예외
    EXIST_CAMP(HttpStatus.BAD_REQUEST, "이미 좋아요한 캠핑장입니다."),
    NOT_FOUND_CAMP(HttpStatus.NOT_FOUND, "해당 캠핑장을 찾을 수 없습니다."),
    NOT_FOUND_CAMP_DETAIL(HttpStatus.NOT_FOUND, "상세검색에 해당하는 캠핑장을 찾을 수 없습니다."),

    //리뷰 예외
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),

    //알림 예외
    NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다."),

    //게시글 예외
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    //댓글 예외
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String detail;
}