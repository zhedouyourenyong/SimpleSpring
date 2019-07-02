package framework.context;

import io.netty.util.concurrent.FastThreadLocal;

public class ContextManager
{
    private static final FastThreadLocal<Context> CONTEXT = new FastThreadLocal();

    public static Context getContext()
    {
        return CONTEXT.get();
    }

    public static void setContext(Context context)
    {
        CONTEXT.set(context);
    }

    public static void removeContext()
    {
        CONTEXT.remove();
    }
}
