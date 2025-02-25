package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import model.Topic;

import java.util.Scanner;

public class Client {

    private final ClientService clientService;

    public Client(ClientService clientService) {
        this.clientService = clientService;
    }

    public void run(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture f = b.connect(host, port).sync();
            System.out.println("Connected to server");

            Channel channel = f.channel();

            // логика ввода пользователем команд
            try (Scanner scanner = new Scanner(System.in);) {
                while (true) {
                    String cmd = scanner.nextLine();

                    if (cmd.equalsIgnoreCase("exit")) {
                        clientService.exit(channel, workerGroup);
                        break;
                    }
                }
            }

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // хост и порт приходят из аргументов
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new Client(new ClientService()).run(host, port);
    }
}
