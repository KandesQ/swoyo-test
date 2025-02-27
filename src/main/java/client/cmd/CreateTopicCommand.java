package client.cmd;

import client.Client;
import dto.TopicDto;
import model.Topic;
import picocli.CommandLine.*;
import util.CommandResolver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Command(name = "create topic", description = "создание топика")
public class CreateTopicCommand implements Runnable {

    @Option(names = {"-n"}, required = true)
    private String topicName;

    @Override
    public void run() {
        TopicDto requestDto = new TopicDto();

        requestDto.setCallingCmd(CommandResolver.getCommandName(CreateTopicCommand.class));
        requestDto.setTopicName(topicName);

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicResponseDto = null;

            if (topicDtoOpt.isPresent()) {
                topicResponseDto = topicDtoOpt.get();
            }

            if (topicResponseDto != null) {
                System.out.println("Topic was created: " + topicResponseDto.getTopicName());
            } else {
                System.out.println("Topic " + topicName + " was not created");
            }

        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());;
        }
    }
}
