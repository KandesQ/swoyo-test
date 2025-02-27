package client.cmd;

import client.Client;
import dto.TopicDto;
import dto.VoteDto;
import model.Topic;
import model.Vote;
import picocli.CommandLine;
import util.CommandResolver;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "vote", description = "голосование")
public class VoteCommand implements Runnable {
    @CommandLine.Option(names = {"-t"}, required = true)
    private String topicName;

    @CommandLine.Option(names = {"-v"}, required = true)
    private String voteName;

    @Override
    public void run() {
        TopicDto requestDto = new TopicDto();

        requestDto.setCallingCmd(CommandResolver.getCommandName(ViewCommand.class));
        requestDto.setTopicName(topicName);
        requestDto.setVoteName(voteName);

        Client.channel.writeAndFlush(requestDto);

        try {
            Optional<TopicDto> topicDtoOpt = Client.clientHandler.waitResponse(10, TimeUnit.SECONDS);
            TopicDto topicResponseDto;

            if (topicDtoOpt.isPresent()) {
                topicResponseDto = topicDtoOpt.get();

                // с моканьем
                try (Scanner scanner = new Scanner(System.in)) {
                    System.out.println("option 1");
                    System.out.println("option 2");
                    System.out.println("option 3");


                    System.out.println("What option will you choose?: ");
                    String option = scanner.nextLine();

                    System.out.println("You chose " + option + "\nAnswer is written");
                }

//                     если бы было без мока
//                 if (topicResponseDto.getCallingCmd().equals("vote")) {
//                     Optional<VoteDto> voteDto = topicResponseDto.getVoteDtos().stream()
//                             .filter(inVoteDto -> inVoteDto.getName().equals(voteName))
//                             .findFirst();
//
//                     if (voteDto.isEmpty()) return;
//
//                     voteDto.get().getOptions().forEach(System.out::println);
//
//                     try (Scanner scanner = new Scanner(System.in)) {
//                         System.out.println("What option will you choose?: ");
//                         String option = scanner.nextLine();
//
//                         System.out.println("You chose " + option + "\nAnswer is written");
//                     }
            }
        } catch (InterruptedException e) {
            System.out.println("Error while response: " + e.getMessage());
        }
    }
}
