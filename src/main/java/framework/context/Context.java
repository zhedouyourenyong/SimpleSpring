package framework.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import framework.constant.Constant;
import framework.protocol.param.Param;
import framework.protocol.response.impl.SimpleHttpResponse;
import framework.protocol.resquest.impl.SimpleHttpRequest;

import java.util.Map;

/*
* 管理当前线程的请求和响应
* */
public final class Context
{

    private SimpleHttpRequest httpRequest;
    private SimpleHttpResponse httpResponse;
    private Param param;

    public Context(SimpleHttpRequest httpRequest, SimpleHttpResponse httpResponse)
    {
        this.httpRequest=httpRequest;
        this.httpResponse=httpResponse;
    }

    public void setParam(Param param)
    {
        this.param=param;
    }

    public Param getParam ()
    {
        return param;
    }

    public SimpleHttpRequest getHttpRequest ()
    {
        return httpRequest;
    }

    public SimpleHttpResponse getHttpResponse ()
    {
        return httpResponse;
    }

    public void json(Object object)
    {
        httpResponse.setContentType(Constant.ContentType.JSON);
        httpResponse.setHttpContent(JSON.toJSONString(object));
    }

    public void html(String htmlPath)
    {
        html(htmlPath,null);
    }

    public void html(String htmlPath, Map<String,Object> model)
    {
        httpResponse.setContentType(Constant.ContentType.HTML);
        httpResponse.setHtmlPath(htmlPath);
        httpResponse.setModel(model);
    }

    public void text(String text)
    {
        httpResponse.setContentType(Constant.ContentType.TEXT);
        httpResponse.setHttpContent(text);
    }
}
