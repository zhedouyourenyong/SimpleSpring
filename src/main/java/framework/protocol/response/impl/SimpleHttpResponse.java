package framework.protocol.response.impl;

import framework.constant.Constant;
import framework.exception.SimpleException;
import framework.protocol.SimpleCookie;
import framework.protocol.response.Response;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpResponse implements Response
{
    private Map<String, String> headers = new HashMap<>();
    private String httpContent;
    private String contentType;
    private String htmlPath;
    private Map<String,Object> model;
    private List<Cookie> cookies = new ArrayList<>();


    private SimpleHttpResponse ()
    {
    }

    public static SimpleHttpResponse init ()
    {
        SimpleHttpResponse response = new SimpleHttpResponse();
        response.contentType = Constant.ContentType.TEXT;
        return response;
    }


    public void setHtmlPath(String htmlPath)
    {
        this.htmlPath=htmlPath;
    }

    public String getHtmlPath()
    {
        return this.htmlPath;
    }

    public void setModel(Map<String,Object> model)
    {
        this.model=model;
    }

    public Map<String,Object> getModel()
    {
        return this.model;
    }


    @Override
    public Map<String, String> getHeaders ()
    {
        return this.headers;
    }

    @Override
    public void setContentType (String contentType)
    {
        this.contentType=contentType;
    }

    @Override
    public String getContentType ()
    {
        return this.contentType;
    }

    @Override
    public void setHttpContent (String content)
    {
        this.httpContent=content;
    }

    @Override
    public String getHttpContent ()
    {
        return this.httpContent;
    }

    @Override
    public void setCookie (SimpleCookie cookie)
    {
        if(null == cookie)
        {
            throw new SimpleException("cookie is null!");
        }

        if(null == cookie.getName())
        {
            throw new SimpleException("cookie.getName() is null!");
        }
        if(null == cookie.getValue())
        {
            throw new SimpleException("cookie.getValue() is null!");
        }

        DefaultCookie defaultCookie = new DefaultCookie(cookie.getName(), cookie.getValue());
        cookie.setPath("/");
        cookie.setMaxAge(cookie.getMaxAge());
        cookies.add(defaultCookie);
    }

    @Override
    public List<Cookie> cookies ()
    {
        return this.cookies;
    }
}
