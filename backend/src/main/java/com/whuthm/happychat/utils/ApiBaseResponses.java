package com.whuthm.happychat.utils;

import com.whuthm.happychat.proto.api.Base;

public enum ApiBaseResponses {

    SUCCESS(0, "ok"),
    INCORRECT_PARAMETERS(1, "incorrect parameters"),
    NOT_FOUND(404, "not found");

    private final int code;
    private final String message;

    ApiBaseResponses(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Base.BaseResponse getResponse() {
        return ApiUtils.getBaseResponse(code, message);
    }

    public Base.BaseResponse getResponse(String hintMessage) {
        return ApiUtils.getBaseResponse(code, message, hintMessage);
    }
}
