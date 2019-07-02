package Test.interceptor;

import framework.annotation.Interceptor;
import framework.context.Context;
import framework.interceptor.SimpleInterceptor;
import framework.protocol.param.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Interceptor(order = 1)
public class ExecuteTimeInterceptor extends SimpleInterceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteTimeInterceptor.class);

    private Long start;

    private Long end;

    @Override
    public boolean before(Context context, Param param) {
        start = System.currentTimeMillis();
        System.out.println("拦截请求");
        LOGGER.info("拦截请求");
        return true;
    }

    @Override
    public void after(Context context,Param param) {
        end = System.currentTimeMillis();
        System.out.println("cast [{}] times:"+( end - start));
        LOGGER.info("cast [{}] times", end - start);
    }
}
