package t_demo.proxy;


import framework.annotation.Aspect;
import framework.proxy.AbsMethodAdvance;

@Aspect(pointCut = "t_demo.proxy.Test,doSomeThing")
public class TestAspect extends AbsMethodAdvance
{

    @Override
    public void doBefore ()
    {
        System.out.println("do before");
    }

    @Override
    public void doAfter ()
    {
        System.out.println("do after");
    }
}