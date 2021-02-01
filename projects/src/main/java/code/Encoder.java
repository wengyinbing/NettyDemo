package code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wengyinbing
 * @data 2021/2/1 18:38
 **/
public class Encoder extends MessageToByteEncoder {
    //private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private static final Logger logger = LoggerFactory.getLogger(Encoder.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        logger.info("编码encode");
        logger.info("处理的数据:{}",msg);
        if(msg instanceof DefaultFullHttpRequest){
            out.writeInt(PackageType.HttpRequest_pack.getCode());
            logger.info("pack code {}",PackageType.HttpRequest_pack.getCode());
        }
        else if(msg instanceof DefaultFullHttpResponse){
            out.writeInt(PackageType.HttpResponse_pack.getCode());
            logger.info("pack code {}",PackageType.HttpResponse_pack.getCode());
        }
        else{
            logger.error("pack error");
        }

        byte[] bytes = new Serializer().serialize(msg);
        out.writeInt(bytes.length);//存长度
        System.out.println("length:"+bytes.length);
        out.writeBytes(bytes);
    }
}
