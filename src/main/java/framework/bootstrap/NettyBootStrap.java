package framework.bootstrap;

import Test.test;
import framework.bean.impl.DefaultBeanFactory;
import framework.config.AppConfig;
import framework.interceptor.InterceptManager;
import framework.route.RouterManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyBootStrap
{
    private static final Logger logger= LoggerFactory.getLogger(NettyBootStrap.class);
    private static final EventLoopGroup workerGroup=new NioEventLoopGroup();
    private static final EventLoopGroup bossGroup=new NioEventLoopGroup();
    private static final ServerBootstrap bootstrap=new ServerBootstrap();
    private static Channel channel;

    private static AppConfig appConfig=AppConfig.getInstance();

    public static void startup() throws Exception
    {
        // start
        startServer();

        // register shutdown hook
        shutDownServer();

        // synchronized channel
        joinServer();
    }

    private static void startServer () throws InterruptedException
    {
        bootstrap.group(bossGroup,workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .handler(new LoggingHandler(LogLevel.INFO))
                 .option(ChannelOption.SO_BACKLOG, 1024)
                 .childOption(ChannelOption.SO_KEEPALIVE, true)
                 .childOption(ChannelOption.TCP_NODELAY, true)
                 .childHandler(new ServerInitializer());

        int port=appConfig.getPort();
        ChannelFuture future = bootstrap.bind(port).sync();

        if(future.isSuccess())
        {
            //todo
            logger.info("监听成功");
        }
        channel=future.channel();
    }

    private static void joinServer () throws Exception
    {
        channel.closeFuture().sync();
    }

    private static void shutDownServer ()
    {
        //todo
    }

//    public static void main (String[] args)
//    {
//        try
//        {
//            String packagetName= test.class.getPackage().getName();
//            DefaultBeanFactory.getInstance().init(packagetName);
//            RouterManager.getInstance().init(packagetName);
//            InterceptManager.getInstance().loadInterceptors(packagetName);
//            NettyBootStrap.startup();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
