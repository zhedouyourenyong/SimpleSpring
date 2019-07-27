package framework.bean;


public interface BeanFactory
{
    void register (Object object);

    Object getBean (String name) throws Exception;

    void releaseBean ();
}
