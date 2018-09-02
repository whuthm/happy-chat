package com.whuthm.happychat.data.api;

import com.google.protobuf.MessageLite;
import com.whuthm.happychat.data.BaseProtos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApiUtils {

    private static final int CODE_SUCCESS = 0;

    public static void requireProtoResponseSuccessful(MessageLite protoResponse) throws
            ApiResponseError,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            NullPointerException {
        final BaseProtos.BaseResponse baseResponse;
        if (protoResponse instanceof BaseProtos.BaseResponse) {
            baseResponse = (BaseProtos.BaseResponse) protoResponse;
        } else {
            final Method getResponseMethod = protoResponse.getClass().getMethod("getResponse");
            baseResponse = (BaseProtos.BaseResponse) getResponseMethod.invoke(protoResponse);
        }
        if (baseResponse.getCode() != CODE_SUCCESS) {
            throw new ApiResponseError(baseResponse.getCode(), baseResponse.getMessage(), protoResponse);
        }
    }
}
