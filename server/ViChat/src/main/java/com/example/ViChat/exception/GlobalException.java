package com.example.ViChat.exception;

import com.example.ViChat.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    private static final String MIN_ATTRIBUTE = "min";

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiResponse> handleException(Exception exception) {
//        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_INVALID.getStatusCode()).body(
//                ApiResponse.builder()
//                        .message(ErrorCode.UNCATEGORIZED_INVALID.getMessage())
//                        .code(ErrorCode.UNCATEGORIZED_INVALID.getCode())
//                        .build()
//        );
//    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        log.info("Hello");
        return ResponseEntity.status(exception.getErrorCode().getStatusCode())
                .body(
                        ApiResponse.builder()
                                .message(exception.getErrorCode().getMessage())
                                .code(exception.getErrorCode().getCode())
                                .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ConstraintViolation constraintViolation =exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
        var attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        log.info(exception.getBindingResult().getAllErrors().toString());
        log.info(enumKey);
        log.info(constraintViolation.toString());
        log.info(attributes.toString());
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiResponse.builder()
                                .message(Objects.nonNull(attributes) ?
                                        mapAttribute(errorCode.getMessage(), attributes)
                                        : errorCode.getMessage()
                                )
                                .code(errorCode.getCode())
                                .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode())
                .body(ApiResponse.builder()
                        .message(ErrorCode.UNAUTHORIZED.getMessage())
                        .code(ErrorCode.UNAUTHORIZED.getCode())
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
