package framework.protocol.response;

import framework.protocol.SimpleCookie;
import io.netty.handler.codec.http.cookie.Cookie;

import java.util.List;
import java.util.Map;

public interface Response
{
    /**
     * get all customer headers
     * @return
     */
    Map<String, String> getHeaders();

    void setContentType(String contentType);

    String getContentType();

    /**
     * set http body
     * @param content
     */
    void setHttpContent(String content);

    /**
     * get http body
     * @return
     */
    String getHttpContent();


    /**
     * set cookie
     * @param cookie cookie
     */
    void setCookie(SimpleCookie cookie) ;


    /**
     * get all cookies
     * @return all cookies
     */
    List<Cookie> cookies() ;

}
