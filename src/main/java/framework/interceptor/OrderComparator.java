package framework.interceptor;

import java.util.Comparator;

public class OrderComparator implements Comparator<SimpleInterceptor>
{
    @Override
    public int compare (SimpleInterceptor o1, SimpleInterceptor o2)
    {
        if(o1.getOrder() <= o2.getOrder())
        {
            return 1;
        }

        return 0;
    }
}
