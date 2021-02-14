/*import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Protocoles extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Пришли данные");
        ByteBuf buf = ((ByteBuf) msg);
        while (buf.isReadable()){
            System.out.print((char) buf.readByte());
        }
        System.out.println();
    }
}
*/