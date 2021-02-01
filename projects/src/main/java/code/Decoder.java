package code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wengyinbing
 * @data 2021/2/1 18:40
 **/
public class Decoder extends ReplayingDecoder {
    private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
    private static  Class<?> packageclass;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // System.out.println("解码decode");
        logger.info("解码decode");
        int pt = in.readInt();
        //pt = 1;
        System.out.println("pt:" + pt);
       if(pt == PackageType.HttpRequest_pack.getCode()){
            packageclass = DefaultFullHttpRequest.class;
        }
        else if(pt == PackageType.HttpResponse_pack.getCode()){
            packageclass = FullHttpResponse.class;
        }
        int length = in.readInt();//协议中传递数据的长度
        System.out.println(length);
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        //logger.info("info");
        Object obj = new Serializer().deserialize(bytes, packageclass);
        out.add(obj);
        logger.info("解码完成");
    }
}