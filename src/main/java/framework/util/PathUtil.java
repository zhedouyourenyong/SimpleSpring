package framework.util;

import framework.config.AppConfig;

public class PathUtil
{
    public static String getRootPath (String path)
    {
        return "/" + path.split("/")[1];
    }

    public static String getControllerPath (String path)
    {
        return path.split("/")[2];
    }

    public static String getRoutePath (String path)
    {
        AppConfig instance = AppConfig.getInstance();
        return path.replace(instance.getRootPackageName(), "");
    }


}
