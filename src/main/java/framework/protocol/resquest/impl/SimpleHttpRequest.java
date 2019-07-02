package framework.protocol.resquest.impl;

import framework.constant.Constant;
import framework.protocol.SimpleCookie;
import framework.protocol.resquest.Request;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

public class SimpleHttpRequest implements Request
{
    private String method;
    private String url;
    private String clientAddress;
    private String body;

    private Map<String, SimpleCookie> cookie = new HashMap<>(8);
    private Map<String, String> headers = new HashMap<>(8);

    private SimpleHttpRequest ()
    {
    }

    public static SimpleHttpRequest init (FullHttpRequest fullHttpRequest)
    {
        SimpleHttpRequest request = new SimpleHttpRequest();
        request.method = fullHttpRequest.method().name();
        request.url = fullHttpRequest.uri();

        initHeaders(request, fullHttpRequest);
        initCookies(request);
        initBody(request, fullHttpRequest);

        return request;
    }

    private static void initHeaders (SimpleHttpRequest request, FullHttpRequest fullHttpRequest)
    {
        for (Map.Entry<String, String> entry : fullHttpRequest.headers().entries())
        {
            request.headers.put(entry.getKey(), entry.getValue());
        }
    }

    private static void initCookies (SimpleHttpRequest request)
    {
        String cookies = request.headers.get(Constant.ContentType.COOKIE);
        if(cookies != null)
        {
            for (io.netty.handler.codec.http.cookie.Cookie cookie : ServerCookieDecoder.LAX.decode(cookies))
            {
                SimpleCookie simpleCookie = new SimpleCookie();
                simpleCookie.setName(cookie.name());
                simpleCookie.setValue(cookie.value());
                simpleCookie.setDomain(cookie.domain());
                simpleCookie.setMaxAge(cookie.maxAge());
                simpleCookie.setPath(cookie.path());
                request.cookie.put(simpleCookie.getName(), simpleCookie);
            }
        }
    }

    private static void initBody (SimpleHttpRequest request, FullHttpRequest fullHttpRequest)
    {
        ByteBuf body = fullHttpRequest.content();
        int length = body.readableBytes();
        byte[] array = new byte[length];
        body.getBytes(body.readerIndex(), array);
        request.body = new String(array, CharsetUtil.UTF_8);
    }


    @Override
    public String getMethod ()
    {
        return this.method;
    }

    @Override
    public String getUrl ()
    {
        return this.url;
    }

    @Override
    public SimpleCookie getCookie (String key)
    {
        return cookie.get(key);
    }

    @Override
    public String getHeader (String key)
    {
        return headers.get(key);
    }

    @Override
    public String getBody ()
    {
        return this.body;
    }
}
