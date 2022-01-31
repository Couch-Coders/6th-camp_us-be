package couch.camping.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String detail;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())//httpStatus 코드
                        .error(errorCode.getHttpStatus().name())//httpStatus 이름
                        .code(errorCode.name())//errorCode 의 이름
                        .detail(errorCode.getDetail())//errorCode 상세
                        .message(e.getMessage())//에러 메시지
                        .build()
                );


    }
}
