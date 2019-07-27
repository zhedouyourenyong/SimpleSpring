package t_demo.interceptor;

import framework.annotation.Interceptor;
import framework.context.Context;
import framework.interceptor.SimpleInterceptor;
import framework.protocol.param.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor(order = 1)
public class LoggerInterceptorAbstract extends SimpleInterceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptorAbstract.class);

    @Override
    public boolean before (Context context, Param param)
    {
//        System.out.println("before "+param.toString());
//        LOGGER.info("before logger param=[{}]", param.toString());
        return true;
    }

    @Override
    public void after (Context context, Param param)
    {
//        System.out.println("after "+param.toString());
//        LOGGER.info("after logger param=[{}]", param.toString());
    }
}
