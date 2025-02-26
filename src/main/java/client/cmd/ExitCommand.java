package client.cmd;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import picocli.CommandLine;

import static client.Client.channel;
import static client.Client.workerGroup;

@CommandLine.Command(name = "exit", description = "exit client")
public class ExitCommand implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("Closing client connection...");
            if (channel != null && channel.isOpen()) {
                channel.close().sync();
            }

            if (!workerGroup.isShutdown()) {
                workerGroup.shutdownGracefully().sync();
            }

            System.out.println("Client connection closed");
        } catch (InterruptedException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
}
