package com.whuthm.happychat.utils;

import com.whuthm.happychat.proto.api.Base;

public class ApiUtils {

    public static Base.BaseResponse getBaseResponse(int code, String message) {
        return Base.BaseResponse
                .newBuilder()
                .setCode(code)
                .setMessage(message)
                .build();
    }

    public static Base.BaseResponse getBaseResponse(int code, String message, String hintMessage) {
        return Base.BaseResponse
                .newBuilder()
                .setCode(code)
                .setMessage(message)
                .setHintMessage(hintMessage)
                .build();
    }

}
