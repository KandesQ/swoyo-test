package client.cmd;

import client.Client;
import dto.TopicDto;
import model.Topic;
import model.Vote;
import picocli.CommandLine;
import util.CommandResolver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Запрашивает топик. Не знаю как реализовать чтобы возвращало много топиков, поэтому -t параметр - обязательный
 */
@CommandLine.Command(name = "view", description = "показывает запрошенные данные")
public class ViewCommand implements Runnable {
    @CommandLine.Option(names = {"-t"}, required = true)
    private String topicName;

    @CommandLine.Option(names = {"-v"})
    private String voteName;

    @Override
    public void run() {
        // должен запросить данные с сервера и ждать пока они придут
        TopicDto requestDto = new TopicDto();

        requestDto.setCallingCmd(CommandResolver.getCommandName(ViewCommand.class));
        requestDto.setTopicName(topicName);

        if (voteName != null) requestDto.setVoteName(voteName);

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicDto;

            if (topicDtoOpt.isPresent()) {
                topicDto = topicDtoOpt.get();

                Topic topic = TopicDto.DtoToModel(topicDto);

                if (voteName != null) {
                    Vote vote = topic.getVotes().stream()
                                    .filter(vote1 -> vote1.getName().equals(voteName))
                            .findFirst()
                            .orElse(new Vote());


                    if (vote.getName() != null && vote.getName().equals(voteName)) {
                        System.out.println("vote description: " + vote.getVoteDescription() +
                                "\noptions: " + vote.getOptions());
                    } else {
                        System.out.println("vote " + voteName + " was not found");
                    }
                } else {
                    System.out.println(topic);
                }
            } else {
                System.out.println("Response time is up");
            }
        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());;
        }
    }
}
