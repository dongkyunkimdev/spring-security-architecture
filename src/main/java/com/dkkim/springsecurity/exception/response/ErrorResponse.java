package com.dkkim.springsecurity.exception.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dkkim.springsecurity.exception.response.ErrorCode.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = new ArrayList<>();
        this.code = code.getCode();
    }


    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(INVALID_TYPE_VALUE, errors);
    }

    public static ErrorResponse of(MissingServletRequestParameterException e) {
        final List<FieldError> errors = FieldError.of(e.getParameterName(), null, e.getMessage());
        return new ErrorResponse(MISSING_VALUE, errors);
    }

    public static ErrorResponse of(IllegalArgumentException e) {
        final List<FieldError> errors = FieldError.of(e.getMessage());
        return new ErrorResponse(INVALID_INPUT_VALUE, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value,
                                          final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(FieldError.builder()
                    .field(field)
                    .value(value)
                    .reason(reason)
                    .build()
            );
            return fieldErrors;
        }

        public static List<FieldError> of(final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(FieldError.builder()
                    .reason(reason)
                    .build()
            );
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult
                    .getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> FieldError.builder()
                            .field(error.getField())
                            .value(
                                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
                            .reason(error.getDefaultMessage())
                            .build()
                    )
                    .collect(Collectors.toList());
        }
    }
}
