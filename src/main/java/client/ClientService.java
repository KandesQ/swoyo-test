package client;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

public class ClientService {

    // все crud методы должны отправлять dto, а с помощью equals() hashCode() будет изменяться data в ServerService

    public void exit(Channel channel, EventLoopGroup group) throws InterruptedException {
        try {
            System.out.println("Closing client connection...");
            if (channel != null && channel.isOpen()) {
                channel.close().sync();
            }

            if (group != null && !group.isShutdown()) {
                group.shutdownGracefully().sync();
            }

            System.out.println("Client connection closed");
        } catch (InterruptedException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }


}
