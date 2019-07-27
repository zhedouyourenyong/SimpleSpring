package framework.bootstrap.handler;

import com.alibaba.fastjson.JSON;
import framework.config.AppConfig;
import framework.constant.Constant;
import framework.context.Context;
import framework.context.ContextManager;
import framework.exception.SimpleException;
import framework.freemarker.FreemarkerUtil;
import framework.interceptor.InterceptManager;
import framework.protocol.param.Param;
import framework.protocol.param.ParamMap;
import framework.protocol.response.WorkRes;
import framework.protocol.response.impl.SimpleHttpResponse;
import framework.protocol.resquest.impl.SimpleHttpRequest;
import framework.route.RouteProcess;
import framework.route.RouterManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


@ChannelHandler.Sharable
public final class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    private final AppConfig appConfig = AppConfig.getInstance();
    private final InterceptManager interceptProcess = InterceptManager.getInstance();
    private final RouterManager routerManager = RouterManager.getInstance();
    private final RouteProcess routeProcess = RouteProcess.getInstance();
    private final FreemarkerUtil freemarkerUtil = FreemarkerUtil.getInstance();

    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception
    {
        SimpleHttpRequest httpRequest = SimpleHttpRequest.init(fullHttpRequest);
        SimpleHttpResponse httpResponse = SimpleHttpResponse.init();

        // set current thread request and response
        ContextManager.setContext(new Context(httpRequest, httpResponse));

        try
        {
            //check root path
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(fullHttpRequest.uri(), "utf-8"));
            appConfig.checkRootPath(httpRequest.getUrl(), queryStringDecoder);

            //build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);
            ContextManager.getContext().setParam(paramMap);

            //interceptor before
            boolean access = interceptProcess.processBefore(paramMap);
            if(!access)
            {
                return;
            }

            // execute Method
            Method method = routerManager.getProcessMethod(queryStringDecoder.path());
            Object result = routeProcess.invoke(method);

            // interceptor after
            interceptProcess.processAfter(paramMap);

        } catch (Exception e)
        {
            exceptionCaught(ctx, e);
        } finally
        {
            responseContent(ctx);
            ContextManager.removeContext();
        }
    }

    private Param buildParamMap (QueryStringDecoder queryStringDecoder)
    {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Map<String, Object> tempMap = new HashMap<>();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet())
        {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            tempMap.put(key, value.get(0));
        }
        Param paramMap = new ParamMap(tempMap);
        return paramMap;
    }

    private void responseContent (ChannelHandlerContext ctx)
    {
        SimpleHttpResponse httpResponse = ContextManager.getContext().getHttpResponse();

        ByteBuf buf = ctx.alloc().ioBuffer();
        String content = null;

        if(httpResponse.getContentType().equals(Constant.ContentType.HTML))
        {
            String path = httpResponse.getHtmlPath();

            Map<String, Object> model = httpResponse.getModel();
            if(model == null)
                model = new HashMap<>(0);

            content = freemarkerUtil.processTemplateIntoString(path, model);
        } else  //text json
        {
            content = httpResponse.getHttpContent();
        }

        buf.writeBytes(content.getBytes(CharsetUtil.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        buildHeader(response);

        ctx.writeAndFlush(response);
    }

    private void buildHeader (DefaultFullHttpResponse response)
    {
        SimpleHttpResponse httpResponse = ContextManager.getContext().getHttpResponse();

        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, httpResponse.getContentType());

        List<Cookie> cookies = httpResponse.cookies();
        for (Cookie cookie : cookies)
        {
            headers.add(Constant.ContentType.SET_COOKIE, io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie));
        }
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        Exception e = (Exception) cause;
        if(SimpleException.isResetByPeer(e.getMessage()))
        {
            return;
        }

        logger.error("Exception", e);

        WorkRes workRes = new WorkRes();
        workRes.setCode("500");
        workRes.setMessage(e.getClass().getName() + "系统运行出现异常");
        Context context = ContextManager.getContext();
        context.json(workRes);
    }
}
