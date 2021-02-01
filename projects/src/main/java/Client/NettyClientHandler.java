package Client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wengyinbing
 * @data 2021/2/1 17:56
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<DefaultFullHttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channelActive");
        //ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2
                //CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             DefaultFullHttpResponse in) {
        logger.info("Client received{}",in );
        //try{
        AttributeKey<DefaultFullHttpResponse> key = AttributeKey.valueOf("data");
        ctx.channel().attr(key).set(in);
        ctx.channel().close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4
        logger.error("Exception!");
        cause.printStackTrace();
        ctx.close();
    }
   /* protected void channelRead0(ChannelHandlerContext ctx, Integer integer) throws Exception {
        try{
            System.out.println("Client received: " + integer);
            AttributeKey<Integer> key = AttributeKey.valueOf("data");
            ctx.channel().attr(key).set(integer);
            ctx.channel().close();}
        finally {
            ReferenceCountUtil.release(integer);
        }
    }*/


}
