package t_demo.controller;


import t_demo.configration.KafkaConfiguration;
import t_demo.service.serviceB;
import com.alibaba.fastjson.JSONObject;
import framework.annotation.AutoWired;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.configuration.ConfigurationManager;
import framework.context.Context;

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
    public void map1(Context context)
    {
        System.out.println("successB!");

        Properties properties= ConfigurationManager.getConfiguration(KafkaConfiguration.class);
        System.out.println(properties.get("kafka.broker.list"));

        JSONObject object=new JSONObject();
        object.put("kafka.broker.list",properties.get("kafka.broker.list"));
        context.json(object);
    }
}
