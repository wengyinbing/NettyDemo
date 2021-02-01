package Client;

import code.Decoder;
import code.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author wengyinbing
 * @data 2021/2/1 17:51
 **/
@AllArgsConstructor
public class NettyClient {
    private final String host = "localhost";
    private final int port = 9000;
    private  final static Bootstrap b = new Bootstrap();;
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    static {
        EventLoopGroup g = new NioEventLoopGroup();

        try{
            b.group(g)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //.remoteAddress(new InetSocketAddress(host,port))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {    //5
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpResponseDecoder())
                                    .addLast(new Encoder())
                                    .addLast(new NettyClientHandler());

                        }
                    });

        }
        catch( Exception e){
            System.out.println("连接客户端出错！");
        }
        finally {
            //g.shutdownGracefully().sync();
        }
    }
    public void start(int data) throws Exception{
        ChannelFuture future = b.connect(host, port).sync();
        logger.info("客户端连接到 {} {}",host,port);
        Channel channel = future.channel();
        URI uri = new URI("/scan/" + "123" + "/start");

        // *** 设置POST数据包中传输的数据 ***
        String content = "hello post" + data;
        FullHttpRequest requestToSQLMAPAPI = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes("UTF-8")));

        requestToSQLMAPAPI.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
        requestToSQLMAPAPI.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        requestToSQLMAPAPI.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                requestToSQLMAPAPI.content().readableBytes());
        requestToSQLMAPAPI.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");

        if(channel != null){
            channel.writeAndFlush(requestToSQLMAPAPI).addListener(future1 -> {
                if(future1.isSuccess()) {
                    logger.info(String.format("客户端发送数据: %s", data));
                } else {
                    logger.error("发送消息时有错误发生: ", future1.cause());
                }
            });
            channel.closeFuture().sync();
            //后面处理服务端返回的数据。
            AttributeKey<DefaultFullHttpResponse> key = AttributeKey.valueOf("data");
            DefaultFullHttpResponse redata = channel.attr(key).get();
            logger.info("服务器返回的数据 {}",redata);
        }

    }

    public static void main(String[] args) throws Exception {
        new NettyClient().start(10);
    }

}
