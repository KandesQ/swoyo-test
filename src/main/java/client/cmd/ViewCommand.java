package client.cmd;

import client.Client;
import dto.TopicDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Topic;
import picocli.CommandLine;
import util.CommandResolver;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Запрашивает топик. Не знаю как реализовать чтобы возвращало много топиков, поэтому -t параметр - обязательный
 */
@CommandLine.Command(name = "view", description = "показывает запрошенные данные")
public class ViewCommand implements Runnable {
    @CommandLine.Option(names = {"-t"}, required = true)
    private String topicName;

    @Override
    public void run() {
        // должен запросить данные с сервера и ждать пока они придут
        TopicDto requestDto = new TopicDto();

        requestDto.setCallingCmd(CommandResolver.getCommandName(ViewCommand.class));
        requestDto.setName(topicName);

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicDto;

            if (topicDtoOpt.isPresent()) {
                topicDto = topicDtoOpt.get();

                Topic topic = TopicDto.DtoToModel(topicDto);
                System.out.println(topic);
            } else {
                System.out.println("Response time is up");
            }
        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());;
        }
    }
}
