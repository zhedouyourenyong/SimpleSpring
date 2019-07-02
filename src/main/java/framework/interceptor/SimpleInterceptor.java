package framework.interceptor;

import framework.context.Context;
import framework.protocol.param.Param;

public abstract class SimpleInterceptor
{
    private int order;

    public int getOrder ()
    {
        return order;
    }

    public void setOrder (int order)
    {
        this.order = order;
    }

    protected abstract boolean before (Context context, Param param);

    protected abstract void after (Context context, Param param);
}
