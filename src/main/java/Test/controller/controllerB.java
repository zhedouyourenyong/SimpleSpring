package Test.controller;


import Test.configration.KafkaConfiguration;
import Test.service.serviceA;
import Test.service.serviceB;
import com.alibaba.fastjson.JSONObject;
import framework.annotation.AutoWired;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.configuration.ConfigurationManager;
import framework.context.Context;
import framework.protocol.param.Param;

import java.util.Properties;

@Controller
public class controllerB
{
    @AutoWired
    serviceB serviceb;

    public void test()
    {
        serviceb.say();
    }

    @RequestMapping(path = "/testB")
    public void map1(Context context, Param param)
    {
        System.out.println("successB!");

        Properties properties= ConfigurationManager.getConfiguration(KafkaConfiguration.class);
        System.out.println(properties.get("kafka.broker.list"));

        JSONObject object=new JSONObject();
        object.put("kafka.broker.list",properties.get("kafka.broker.list"));
        context.json(object);
    }
}
