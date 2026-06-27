package com.grocio.backend.common.response;

import com.grocio.backend.common.util.DateTimeUtil;
import com.grocio.backend.common.util.RequestIdGenerator;

public final class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ApiResponse<T> success(T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .timestamp(DateTimeUtil.now())
                .requestId(RequestIdGenerator.generate())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(DateTimeUtil.now())
                .requestId(RequestIdGenerator.generate())
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {

        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(DateTimeUtil.now())
                .requestId(RequestIdGenerator.generate())
                .build();
    }

}