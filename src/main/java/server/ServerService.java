package server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.util.List;

public class ServerService {

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
}
