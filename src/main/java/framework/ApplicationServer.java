package framework;

import framework.bootstrap.NettyBootStrap;
import framework.config.ApplicationInitSetting;


public final class ApplicationServer
{

    public static void start (Class<?> clazz, String path) throws Exception
    {
        ApplicationInitSetting.setting(clazz, path);
        NettyBootStrap.startup();
    }

    public static void start (Class<?> clazz) throws Exception
    {
        start(clazz, null);
    }

}
