package t_demo.controller;


import com.alibaba.fastjson.JSONObject;
import t_demo.proxy.Test;
import t_demo.service.serviceA;
import framework.annotation.AutoWired;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.constant.Constant;
import framework.context.Context;
import framework.protocol.response.impl.SimpleHttpResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
public class controllerA
{
    @AutoWired
    serviceA servicea;

    @AutoWired
    Test test;

    @RequestMapping(path = "/testA")
    public void map1(Context context)
    {
        Map<String,Object> model=new HashMap<>();
        model.put("name","zzh");

        context.html("test.html",model);
    }

    @RequestMapping(path = "/test")
    public void map3(Context context)
    {
        context.html("test.html");
    }

    @RequestMapping(path = "/testAP")
    public void map2(Context context)
    {
        test.doSomeThing();
        test.doWtihNotProxy();

        JSONObject json=new JSONObject();
        json.put("msg",0);
        context.json(json);
    }

}
