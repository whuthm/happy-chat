package com.whuthm.happychat.imlib.model;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by huangming on 2018/9/6.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageTag {
    //透传消息
    int FLAG_NONE = 0;
    // 保存在本地
    int FLAG_PERSISTED = 1;
    // 保存在本地并计算未读数
    int FLAG_COUNTED = 3;

    String TYPE_TXT = "txt";
    String TYPE_IMG = "img";

    String type();

    int flag() default FLAG_NONE;

}
