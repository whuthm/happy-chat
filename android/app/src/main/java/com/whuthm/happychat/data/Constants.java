package com.whuthm.happychat.data;

import com.whuthm.happychat.BuildConfig;

/**
 * 常量配置
 * 
 * Created by tanwei on 2018/7/19.
 */

public class Constants {
    
    public static final int HTTP_TIMEOUT = 10;// unit: second
    
    // 模拟器访问127.0.0.1访问不成功，需要使用本机ip地址映射10.0.2.2
    // 正常网址是chat.whuthm.com
    public static final String BASE_URL = BuildConfig.API_URL;

    public static final String HTTP_CACHE_DIR = "http_cache";
    
    public static final int HTTP_CACHE_SIZE = 5 * 1024 * 1024;
    
    public static final String KEY_CONVERSATION_ID = "conversation_id";
    
    public static final int MESSAGE_PAGE_COUNT = 20;// 消息分页加载量
    
    // 消息和会话的事件定义
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int ACTION_DELETE = 3;
}
