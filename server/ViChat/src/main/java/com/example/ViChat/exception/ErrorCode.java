package com.example.ViChat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USERNAME_EXISTED(1001, "Username existed", HttpStatus.BAD_REQUEST),
    PASSWORD_MISS_MATCH(1002, "Password and retype password not match", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(1003, "Role not exist", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(1004, "User not exist", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_INVALID(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1006, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1007, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.UNAUTHORIZED),
    DOB_INVALID(1009, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ORIGINAL_PASSWORD_INVALID(1010, "Incorrect password", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EMPTY(1013, "Email cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXIST(1014, "Email not exist", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1015, "Email existed", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1016, "Email or password is incorrect. Please try again", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED(1017, "Your account has been locked", HttpStatus.FORBIDDEN),
    CHAT_NOT_EXIST(1018, "Chat not found", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHORIZED_TO_ADD_USER_TO_GROUP(1019,  "User is not authorized to add other users to the group", HttpStatus.BAD_REQUEST),
    USER_NOT_MEMBER_GROUP(1020, "You are not member of this", HttpStatus.BAD_REQUEST),
    USER_NOT_ADMIN_GROUP(1021, "You are not admin of this", HttpStatus.BAD_REQUEST),
    MESSAGE_NOT_FOUND(1022, "Message not found", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_MESSAGE_DELETION(1023, "User does not have permission to delete this message", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1024, "Role have been existed", HttpStatus.BAD_REQUEST),
    VERIFY_CODE_INCORRECT(1025, "Verify code incorrect", HttpStatus.BAD_REQUEST),
    VERIFY_CODE_HAS_EXPIRED(1026, "Verify code has expired", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
