package framework.config;

import framework.enums.StatusEnum;
import framework.exception.SimpleException;
import framework.util.PathUtil;
import io.netty.handler.codec.http.QueryStringDecoder;


public final class AppConfig
{
    private String rootPackageName;
    private String rootPath;
    private Integer port;

    private AppConfig ()
    {
    }

    private static AppConfig config;
    public static AppConfig getInstance ()
    {
        if(config == null)
        {
            config = new AppConfig();
        }
        return config;
    }

    public String getRootPackageName ()
    {
        return rootPackageName;
    }

    public void setRootPackageName (Class<?> clazz) throws Exception
    {
        if(clazz.getPackage() == null)
        {
            throw new SimpleException("Your main class is empty of package");
        }
        this.rootPackageName = clazz.getPackage().getName();
    }

    public String getRootPath ()
    {
        return rootPath;
    }

    public void setRootPath (String rootPath)
    {
        this.rootPath = rootPath;
    }

    public Integer getPort ()
    {
        return port;
    }

    public void setPort (Integer port)
    {
        this.port = port;
    }


    /**
     * check Root Path
     */
    public void checkRootPath (String uri, QueryStringDecoder queryStringDecoder) throws Exception
    {
        String nowPath=PathUtil.getRootPath(queryStringDecoder.path());
        String targetPath=this.getRootPath();
        if(!nowPath.equals(targetPath))
        {
            throw new SimpleException(StatusEnum.REQUEST_ERROR, uri);
        }
    }
}
