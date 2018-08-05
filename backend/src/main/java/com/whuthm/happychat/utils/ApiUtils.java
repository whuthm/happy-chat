package com.whuthm.happychat.utils;

import com.whuthm.happychat.data.BaseProtos;

public class ApiUtils {

    public static BaseProtos.BaseResponse getBaseResponse(int code, String message) {
        return BaseProtos.BaseResponse
                .newBuilder()
                .setCode(code)
                .setMessage(message)
                .build();
    }

    public static BaseProtos.BaseResponse getBaseResponse(int code, String message, String hintMessage) {
        return BaseProtos.BaseResponse
                .newBuilder()
                .setCode(code)
                .setMessage(message)
                .setHintMessage(hintMessage)
                .build();
    }

}
