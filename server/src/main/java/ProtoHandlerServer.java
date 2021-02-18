import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProtoHandlerServer extends ChannelInboundHandlerAdapter {
    public enum State {
        IDLE, NAME_LENGTH, NAME, FILE_LENGTH, FILE, MESSAGE_LENGTH, MESSAGE, SERVER_COMMAND_LENGTH, SERVER_COMMAND
    }

    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

    private StringBuilder stringBuilder = new StringBuilder();

    ServerControl svrc = new ServerControl();

    public ProtoHandlerServer() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        while (buf.readableBytes() > 0) {
            if (currentState == State.IDLE) {
                byte readed = buf.readByte();
                if (readed == (byte) 25) {
                    currentState = State.NAME_LENGTH;
                    receivedFileLength = 0L;
                    System.out.println("STATE: Start file receiving");
                } else if (readed == (byte) 24) {
                    System.out.println("REGUEST");
                    while (buf.isReadable()){
                        stringBuilder.append((char) buf.readByte());
                    }
                    String fileName = String.valueOf(stringBuilder);
                    System.out.println(fileName);
                    Path path = Paths.get(svrc.getCurrentDir()+"\\"+fileName);
                    ProtoFileSender.sendFile(path, ctx.channel(), future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("Файл успешно передан");
                        }
                    });
                    System.out.println("-------------------------------------------------");
                } else if (readed == (byte) 23) {
                    currentState = State.MESSAGE_LENGTH;
                } else if (readed == (byte) 22) {
                    currentState = State.SERVER_COMMAND_LENGTH;
                } else {
                    System.out.println("ERROR: Invalid first byte - " + readed);
                }
            }

            if (currentState == State.SERVER_COMMAND_LENGTH) {
                if (buf.readableBytes() >= 4) {
                    nextLength = buf.readInt();
                    currentState = State.SERVER_COMMAND;
                }
            }

            if(currentState == State.SERVER_COMMAND){
                if (buf.readableBytes() >= nextLength) {
                    System.out.println(nextLength);
                    byte [] message = new byte[nextLength];
                    for (int i = 0; i < message.length; i++) {
                        message[i] = buf.readByte();
                    }
                    String str = new String(message, "UTF-8");
                    svrc.setCtx(ctx);
                    svrc.command(str);
                    currentState = State.IDLE;
                }
            }

            if (currentState == State.MESSAGE_LENGTH) {
                if (buf.readableBytes() >= 4) {
                    nextLength = buf.readInt();
                    currentState = State.MESSAGE;
                }
            }

            if(currentState == State.MESSAGE){
                if (buf.readableBytes() >= nextLength) {
                    System.out.println(nextLength);
                    byte [] message = new byte[nextLength];
                    for (int i = 0; i < message.length; i++) {
                        message[i] = buf.readByte();
                    }
                    String str = new String(message, "UTF-8");
                    System.out.println(str);
                    currentState = State.IDLE;
                }
            }

            if (currentState == State.NAME_LENGTH) {
                if (buf.readableBytes() >= 4) {
                    System.out.println("STATE: Get filename length");
                    nextLength = buf.readInt();
                    currentState = State.NAME;
                }
            }

            if (currentState == State.NAME) {
                if (buf.readableBytes() >= nextLength) {
                    byte[] fileName = new byte[nextLength];
                    buf.readBytes(fileName);
                    System.out.println("STATE: Filename received - _" + new String(fileName, "UTF-8"));
                    out = new BufferedOutputStream(new FileOutputStream("_" + new String(fileName)));
                    currentState = State.FILE_LENGTH;
                }
            }

            if (currentState == State.FILE_LENGTH) {
                if (buf.readableBytes() >= 8) {
                    fileLength = buf.readLong();
                    System.out.println("STATE: File length received - " + fileLength);
                    currentState = State.FILE;
                }
            }

            if (currentState == State.FILE) {
                while (buf.readableBytes() > 0) {
                    out.write(buf.readByte());
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        currentState = State.IDLE;
                        System.out.println("File received");
                        out.close();
                        break;
                    }
                }
            }
        }
        if (buf.readableBytes() == 0) {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
