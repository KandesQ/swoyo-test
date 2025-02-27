package client.cmd;

import client.Client;
import dto.TopicDto;
import picocli.CommandLine;
import util.CommandResolver;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "create vote", description = "осздает голосование")
public class CreateVoteCommand implements Runnable {

    @CommandLine.Option(names = "-t", required = true)
    private String topicName;

    @Override
    public void run() {
        TopicDto requestDto = new TopicDto();


        requestDto.setCallingCmd(CommandResolver.getCommandName(CreateVoteCommand.class));
        requestDto.setTopicName(topicName);


        String voteName = "";

        Scanner scanner = Client.scanner;

        System.out.print("Vote name: ");
        voteName = scanner.nextLine();

        System.out.print("Vote description: ");
        String voteDesc = scanner.nextLine();

        System.out.print("Vote option amount: ");
        String optionAmount = scanner.nextLine();

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicResponseDto = null;

            if (topicDtoOpt.isPresent()) {
                topicResponseDto = topicDtoOpt.get();
            }

            System.out.println("Topic " + voteName + " has been created");

        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());
            ;
        }
    }
}
