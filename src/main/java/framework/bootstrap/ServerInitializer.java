package framework.bootstrap;

import framework.bootstrap.handler.DispatcherHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitializer extends ChannelInitializer<NioSocketChannel>
{
    private static final DispatcherHandler disPatherHandler=new DispatcherHandler();

    @Override
    protected void initChannel (NioSocketChannel channel) throws Exception
    {
        ChannelPipeline cp=channel.pipeline();
        cp.addLast("HttpServerCodec",new HttpServerCodec());
        cp.addLast("HttpObjectAggregator",new HttpObjectAggregator(10*1024*1024));
        cp.addLast("ChunkedWriteHandler",new ChunkedWriteHandler());
        cp.addLast("DispatcherHandler",disPatherHandler);
    }
}
