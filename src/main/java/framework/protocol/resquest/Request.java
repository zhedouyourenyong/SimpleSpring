package framework.protocol.resquest;

import framework.protocol.SimpleCookie;

public interface Request
{
    /**
     * get Request method
     * @return
     */
    String getMethod() ;

    /**
     * get Request url
     * @return
     */
    String getUrl() ;

    /**
     * get cookie by key
     * @param key
     * @return return cookie by key
     */
    SimpleCookie getCookie(String key) ;

    //请求头
    String getHeader(String key);

    //请求体
    String getBody();
}
