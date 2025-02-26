package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.TopicDto;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import model.Topic;
import model.Vote;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import util.CommandResolver;

public class ServerService {
    private static Map<String, Map<String, Map<String, String>>> data;
    private Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;

    public ServerService() {

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Topic topic = new Topic("topic");

        topic.setVotes(List.of(
                new Vote("vote1", "gagdsdfgd", 31, List.of("Sf", "ASf", "FE")),
                new Vote("vote1", "gagdsdfgd", 31, List.of("Sf", "ASf", "FE")),
                new Vote("vote1", "gagdsdfgd", 31, List.of("Sf", "ASf", "FE"))

        ));

        topics.put(topic.getName(), topic);

        System.out.println("Checking the data from last session...");
        load("data.json");
    }

    /**
     * Определяет какую логику вызвать, исходя из команды
     */
    public void resolveAndFlush(ChannelHandlerContext ctx, TopicDto topicDto) {
        TopicDto response = new TopicDto();


        switch (topicDto.getCallingCmd()) {
            case "view":
                if (!topics.containsKey(topicDto.getName())) {
                    System.out.println("topic " + topicDto.getName() + " doesn't exist");
                } else {
                    response = TopicDto.ModelToDto(topics.get(topicDto.getName()));
                    System.out.println("Topic" + response.getName() + " sent");
                }
                break;
            default:
                System.out.println("couldn't proccess command " + topicDto.getCallingCmd());
        }

        ctx.writeAndFlush(response);
    }

    /**
     * Изменяет/Добавляет топик в data
     */
    public static void update(TopicDto topicDto) {}

    public void exit(ChannelFuture future, List<EventLoopGroup> groups) throws InterruptedException {
        try {
            System.out.println("Closing server...");
            future.channel().close().sync();
            for (var group : groups) {
                group.shutdownGracefully().sync();
            }
            System.out.println("Server is closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename) {
//        File dataFile = new File(filename);
//
//        // логика загрузки данных если они есть, если файла нет, то просто пустая мапа
//        if (dataFile.exists() && dataFile.isFile()) {
//            try {
//                data = mapper.readValue(
//                        dataFile,
//                        new TypeReference<ConcurrentHashMap<String, Map<String, Map<String, String>>>>() {}
//                );
//                System.out.println("Data restored");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            System.out.println("No data found. Initialized empty map");
//            data = new ConcurrentHashMap<>();
//        }
    }

    public void save(String filename) {
//        System.out.println("Saving current data...");
//
//        Map<String, String> voteInfo = new HashMap<>();
//        voteInfo.put("desc", "somedesc");
//        voteInfo.put("optionAmount", "25");
//        voteInfo.put("options", String.valueOf(List.of("пнуть бомжа", "пройти в окно", "курнуть косячка")));
//
//        Map<String, Map<String, String>> voteMap = new HashMap<>();
//
//        voteMap.put("vote", voteInfo);
//
//        data.put("topic", voteMap);
//
////         перевод data в строковый формат...
//
//        try {
//            mapper.writeValue(new File(filename), data);
//        } catch (IOException e) {
//            System.out.println("Couldn't save data: " + e.getMessage());
//        }
//
//        System.out.println("Saved");
    }

}
