package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.TopicDto;
import dto.VoteDto;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import model.Topic;
import model.Vote;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerService {
    private Map<String, Topic> topics;
    private final ObjectMapper mapper;

    public ServerService() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

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
                if (!topics.containsKey(topicDto.getTopicName())) {
                    System.out.println("topic " + topicDto.getTopicName() + " doesn't exist");
                } else {
                    response = TopicDto.ModelToDto(topics.get(topicDto.getTopicName()));
                }
                break;
            default:
                System.out.println("couldn't proccess command " + topicDto.getCallingCmd());
        }

        ctx.writeAndFlush(response);
        System.out.println("Topic" + response.getTopicName() + " sent");
    }

    public void exit(ChannelFuture future, List<EventLoopGroup> groups) throws InterruptedException {
        // cначала сохраняем данные
        save("data.json");
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
        topics = new ConcurrentHashMap<>();

        Map<String, Map<String, Map<String, String>>> data; // HashMap потому что с многопотоком уже не работает

        if (!dataFile.exists() || !dataFile.isFile()) {
            System.out.println("No data found. Initialized empty map");
            return;
        }


        try {
            //  в промежуточную структуру
            data = mapper.readValue(
                    dataFile,
                    new TypeReference<HashMap<String, Map<String, Map<String, String>>>>() {
                    }
            );

            for (Map.Entry<String, Map<String, Map<String, String>>> topicEntry : data.entrySet()) {
                String topicName = topicEntry.getKey();
                Map<String, Map<String, String>> voteMap = topicEntry.getValue();

                Topic topic = new Topic();
                topic.setName(topicName);
                List<Vote> votes = new ArrayList<>();

                for (Map.Entry<String, Map<String, String>> voteEntry : voteMap.entrySet()) {
                    String voteName = voteEntry.getKey();
                    Map<String, String> voteFields = voteEntry.getValue();

                    Vote vote = new Vote();
                    try {
                        Field nameField = vote.getClass().getDeclaredField("name");
                        nameField.setAccessible(true);
                        nameField.set(vote, voteName);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.out.println("Error setting vote name: " + e.getMessage());
                    }
                    for (Map.Entry<String, String> fieldEntry : voteFields.entrySet()) {
                        String fieldName = fieldEntry.getKey();
                        String stringValue = fieldEntry.getValue();
                        try {
                            Field field = vote.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object convertedValue = convertValue(stringValue, field.getType());
                            field.set(vote, convertedValue);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.out.println("Error setting field " + fieldName + ": " + e.getMessage());
                        }
                    }
                    votes.add(vote);
                }
                topic.setVotes(votes);
                topics.put(topicName, topic);
            }
            System.out.println("Data restored from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();

            System.out.println("Data restored");
        }

    }

    /**
     * Преобразует строковое представление в требуемый тип.
     * Поддерживаются String, int, Integer и List (для List<String>).
     */
    private Object convertValue(String value, Class<?> type) {
        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(List.class)) {
            value = value.trim();
            if (value.startsWith("[") && value.endsWith("]")) {
                value = value.substring(1, value.length() - 1);
            }
            if (value.isEmpty()) {
                return new ArrayList<String>();
            }
            String[] parts = value.split(",\\s*");
            return new ArrayList<>(Arrays.asList(parts));
        }
        // Для остальных типов можно добавить дополнительные преобразования по необходимости.
        return value;
    }

    public void save(String filename) {
        System.out.println("Saving current data...");

        Map<String, Map<String, Map<String, String>>> data = new HashMap<>(); // HashMap потому что с многопотоком уже не работает


        for (Topic topic: topics.values()) {
            Map<String, Map<String, String>> voteMap = new HashMap<>();
            for (Vote vote: topic.getVotes()) {
                Map<String, String> voteInfo = new HashMap<>();

                for (Field voteField: vote.getClass().getDeclaredFields()) {
                    voteField.setAccessible(true);
                    try {
                        voteInfo.put(voteField.getName(), String.valueOf(voteField.get(vote)));
                    } catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }

                voteMap.put(vote.getName(), voteInfo);
            }

            data.put(topic.getName(), voteMap);
        }
        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.out.println("Couldn't save data: " + e.getMessage());
        }

        System.out.println("Saved");
    }

}
