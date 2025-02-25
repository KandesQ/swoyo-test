package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServerService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void exit(ChannelFuture future, List<EventLoopGroup> groups) throws InterruptedException {
        try {
            System.out.println("Closing server...");
            future.channel().close().sync();
            for (var group: groups) {
                group.shutdownGracefully().sync();
            }
            System.out.println("Server is closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename) {}

    public void save(String filename, Map<String, Map<String, Integer>> data) {
        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.out.println("Couldn't save data: " + e.getMessage());
        }
    }

}
