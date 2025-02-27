package client.cmd;

import client.Client;
import dto.TopicDto;
import picocli.CommandLine;
import util.CommandResolver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "delete", description = "delete the vote")
public class DeleteCommand implements Runnable {
    @CommandLine.Option(names = {"-t"}, required = true)
    private String topicName;

    @CommandLine.Option(names = {"-v"}, required = true)
    private String voteName;

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
                System.out.println("Topic was deleted: " + topicResponseDto.getTopicName());
            } else {
                System.out.println("Topic " + topicName + " was not deleted");
            }

        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());;
        }
    }
}
