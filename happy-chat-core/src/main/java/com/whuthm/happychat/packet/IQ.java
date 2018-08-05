package com.whuthm.happychat.packet;

import java.io.Serializable;

/**
 * Info/Query
 */
public class IQ implements Serializable  {
    private static final long serialVersionUID = -148655570445221047L;

    private Type type;


    public enum Type {
        error,
        set,
        get,
        result
    }

}
