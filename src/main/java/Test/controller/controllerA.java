package Test.controller;


import Test.service.serviceA;
import com.alibaba.fastjson.JSONObject;
import framework.annotation.AutoWired;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.constant.Constant;
import framework.context.Context;
import framework.protocol.param.Param;
import framework.protocol.response.impl.SimpleHttpResponse;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
public class controllerA
{
    @AutoWired
    serviceA servicea;

    @RequestMapping(path = "/testA")
    public void map1(Context context, Param param)
    {
        servicea.say();

        SimpleHttpResponse response=context.getHttpResponse();
        response.setContentType(Constant.ContentType.HTML);
        response.setHtmlPath("test.html");
        Map<String,Object> model=new HashMap<>();
        model.put("name","zzh");
        response.setModel(model);
    }

}
