package com.whuthm.happychat.service.connection;

import javax.websocket.CloseReason;

/**
 * code > 2999 && code < 5000
 * {@link CloseReason.CloseCodes}
 */
public enum ConnectionCloseCodes {

    /**
     * 被踢掉
     */
    kicked(3000, "kicked"),
    /**
     * token不正确
     */
    token_incorrect(3001, "token_incorrect"),
    /**
     * token过期
     */
    token_expired(3002, "token_expired");


    private final int code;
    private final String message;

    ConnectionCloseCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public CloseReason.CloseCode getCloseCode() {
        return CloseReason.CloseCodes.getCloseCode(code);
    }

    public CloseReason getCloseReason() {
        return new CloseReason(CloseReason.CloseCodes.getCloseCode(code), message);
    }

}
