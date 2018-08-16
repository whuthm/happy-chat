package com.whuthm.happychat.data;

/**
 * 常量配置
 * 
 * Created by tanwei on 2018/7/19.
 */

public class Constants {
    
    public static final int HTTP_TIMEOUT = 10;// unit: second
    
    // 模拟器访问127.0.0.1访问不成功，需要使用本机ip地址映射10.0.2.2
    // 正常网址是chat.whuthm.com
    public static final String BASE_URL = "http://chat.whuthm.com:8080";
    
    public static final String HTTP_CACHE_DIR = "http_cache";
    
    public static final int HTTP_CACHE_SIZE = 5 * 1024 * 1024;
    
    public static enum ConversationType {
        SINGLE, GROUP,
    }
    
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NICK = "user_nick";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_KEYSTORE = "keystore";

    public static final String KEY_CONVERSATION_ID = "conversation_id";

    public static final int MESSAGE_PAGE_COUNT = 20;// 消息分页加载量
}
