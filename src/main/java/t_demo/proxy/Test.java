package t_demo.proxy;


import framework.annotation.Service;

@Service
public class Test
{

    public void doSomeThing ()
    {
        System.out.println("do some thing...");
    }

    public void doWtihNotProxy ()
    {
        System.out.println("do some thing with not proxy");
    }
}
