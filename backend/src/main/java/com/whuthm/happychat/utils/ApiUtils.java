package com.whuthm.happychat.utils;

import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.exception.ServerException;

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

    public static BaseProtos.BaseResponse getErrorResponse(Exception error) {
        if (error instanceof ServerException) {
            return ApiBaseResponses.SERVER_ERROR.getResponse();
        } else if (error instanceof IllegalArgumentException) {
            return ApiBaseResponses.INCORRECT_PARAMETERS.getResponse();
        } else {
            return ApiBaseResponses.UNKNOWN_ERROR.getResponse();
        }
    }

}
