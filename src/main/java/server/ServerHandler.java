package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Topic;

import java.sql.SQLOutput;

/**
 * Обрабатывает запросы на изменение топика от клиента
 */
public class ServerHandler extends SimpleChannelInboundHandler<Topic> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Topic topic) {
        System.out.println("Received object from client: " + topic);
        ctx.writeAndFlush(topic + " is received");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
