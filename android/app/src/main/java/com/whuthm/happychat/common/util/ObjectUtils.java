package com.whuthm.happychat.common.util;

import java.util.Arrays;

/**
 * Created by huangming on 2017/6/3.
 * 对象工具类
 */

public class ObjectUtils {

    private ObjectUtils() {

    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }


    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

}
