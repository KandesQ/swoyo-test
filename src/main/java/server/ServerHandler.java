package server;

import client.ClientService;
import dto.TopicDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Topic;

/**
 * Обрабатывает запросы на изменение топика от клиента
 */
public class ServerHandler extends SimpleChannelInboundHandler<TopicDto> {

    private final ServerService serverService;

    public ServerHandler(ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * Принимает топик, который добавить/удалить/изменить в data
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TopicDto topicDto) {
        System.out.println("Request: " + topicDto);
        serverService.resolveAndFlush(ctx, topicDto);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("An error occurred: " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
