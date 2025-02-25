package server;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerService {
    private Map<String, Map<String, Integer>> data;
    private final ObjectMapper mapper = new ObjectMapper();

    public ServerService() {
        System.out.println("Checking the data from last session...");
        load("data.json");
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
                        new TypeReference<ConcurrentHashMap<String, Map<String, Integer>>>() {}
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

        data.put("topic1", Map.of("vote1", 25, "vote2", 41));

        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.out.println("Couldn't save data: " + e.getMessage());
        }
    }

}
