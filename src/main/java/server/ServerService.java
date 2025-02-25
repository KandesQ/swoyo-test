package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.TopicDto;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import model.Topic;
import model.Vote;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerService {
    private static Map<String, Map<String, Map<String, String>>> data;
    private final ObjectMapper mapper;

    public ServerService() {

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println("Checking the data from last session...");
        load("data.json");
    }

    /**
     * Изменяет/Добавляет топик в data
     */
    public static void update(TopicDto topicDto) {
        Topic topic = TopicDto.DtoToModel(topicDto);

        // voteName, voteInfo
        Map<String, Map<String, String>> voteMap = new HashMap<>();

        for (var vote: topic.getVotes()) {
            // voteFieldName, voteFieldAttribute
            Map<String, String> voteInfo = new HashMap<>();

            for (var voteField: vote.getClass().getFields()) {
                voteField.setAccessible(true);

                // не берем имя тк это ключ
                if (voteField.getName().equalsIgnoreCase(vote.getName())) continue;

                try {
                    voteInfo.put(voteField.getName(), String.valueOf(voteField.get(vote)));
                } catch (IllegalAccessException e) {
                    System.out.println("Couldn't access to field " + voteField.getName() + ": " + e.getMessage());
                }
            }

            // topicName, voteMap
            voteMap.put(vote.getName(), voteInfo);
        }

        data.put(topic.getName(), voteMap);
    }

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
        File dataFile = new File(filename);

        // логика загрузки данных если они есть, если файла нет, то просто пустая мапа
        if (dataFile.exists() && dataFile.isFile()) {
            try {
                data = mapper.readValue(
                        dataFile,
                        new TypeReference<ConcurrentHashMap<String, Map<String, Map<String, String>>>>() {}
                );
                System.out.println("Data restored");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No data found. Initialized empty map");
            data = new ConcurrentHashMap<>();
        }
    }

    public void save(String filename) {
        System.out.println("Saving current data...");

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

        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.out.println("Couldn't save data: " + e.getMessage());
        }

        System.out.println("Saved");
    }

}
