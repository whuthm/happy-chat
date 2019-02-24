package com.whuthm.happychat.utils;

public class Constants {


    /**
     * token有效期（小时）
     */
    public static final int TOKEN_EXPIRES_HOUR = 30 * 24;

    public static final String HEADER_TOKEN = "token";
    public static final String HEADER_USER_ID = "user_id";
    public static final String HEADER_CLIENT_RESOURCE = "client_resource";

    public static final String IDENTIFIER_DOMAIN_CHAT = "im";
    public static final String IDENTIFIER_DOMAIN_AUTH = "auth";

    public static final String REGEX_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    //^                         # Start of the line
    //[a-zA-Z][0-9a-zA-Z]       # Match characters and symbols in the list, a-z, 0-9, A-Z
    //{6, 15}                   # Length at least 6 characters and maximum length of 15
    //$                         # End of the line
    public static final String REGEX_USERNAME = "^[a-zA-Z][0-9a-zA-Z]{6, 15}$";

    //检测密码由6-21字母和数字组成，不能是纯数字或纯英文
    public static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

}
