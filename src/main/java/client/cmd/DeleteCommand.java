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
        requestDto.setVoteName(voteName);

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicResponseDto = null;

            if (topicDtoOpt.isPresent()) {
                topicResponseDto = topicDtoOpt.get();
            }

            // 1 - есть истекло время ожидания, 2 - если сервер не нашел такого топика или такого голосования, 3 - если нашел и удалил
            if (topicResponseDto != null && topicResponseDto.getCallingCmd().equals("delete") && topicResponseDto.getVoteName().equals(voteName)) {
                System.out.println("Vote " + voteName + " of topic: " + topicName + " was deleted");
            } else {
                System.out.println("Vote " + voteName + " was not deleted");
            }

        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());;
        }
    }
}
