package server;

import dto.TopicDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Topic;

/**
 * Обрабатывает запросы на изменение топика от клиента
 */
public class ServerHandler extends SimpleChannelInboundHandler<TopicDto> {

    /**
     * Принимает топик, который добавить/удалить/изменить в data
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TopicDto topicDto) {
        // будет вызываться метод сервиса update для доавбления/изменения топика в мапе data
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
