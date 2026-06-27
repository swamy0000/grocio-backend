package com.grocio.backend.common.util;

import java.util.UUID;

public final class RequestIdGenerator {

    private RequestIdGenerator() {
    }

    public static String generate() {

        return "REQ-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();

    }

}