package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.List;
import java.util.Scanner;

public class Server {

    private final ServerService serverService;

    public Server(ServerService serverService) {
        this.serverService = serverService;
    }

    public void run(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(
//                                    new LoggingHandler(LogLevel.INFO)
//                            );
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null))
                            );
                            socketChannel.pipeline().addLast(
                                    new ObjectEncoder()
                            );
                            socketChannel.pipeline().addLast(
                                    new ServerHandler(serverService)
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server is started on port: " + port);

            // создаю поток, потому что сервер блокируется для ожидания закрытия канала
            new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in);) {
                    while (true) {
                        String cmd = scanner.nextLine();
                        if (cmd.equalsIgnoreCase("exit")) {
                            try {
                                serverService.exit(f, List.of(workerGroup, bossGroup));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }).start();

            // вот тут
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        // возможно указать кастомный порт в аргументах
        if (args.length > 0) port = Integer.parseInt(args[0]);

        new Server(new ServerService()).run(port);
    }
}
