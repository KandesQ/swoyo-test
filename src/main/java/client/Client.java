package client;

import client.cmd.LoginCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import util.CommandResolver;

import java.util.Scanner;

public class Client {

    private final ClientService clientService;
    private final CommandResolver commandResolver;

    public static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    public static Channel channel;

    private String username;

    public Client(ClientService clientService, CommandResolver commandResolver) {
        this.clientService = clientService;
        this.commandResolver = commandResolver;
    }

    public void run(String host, int port) throws InterruptedException {
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

            channel = f.channel();

            // логика ввода пользователем команд
            try (Scanner scanner = new Scanner(System.in);) {
//                do {
//                    System.out.println("Before using app, please, log in");
//                    String[] parsedCmd = scanner.nextLine().split(" ");
//                    commandResolver.getCommand(CommandResolver.getCommandName(LoginCommand.class)).execute(parsedCmd);
//                } while (username == null);

                while (true) {
                    System.out.print(">>> ");

                    String[] parsedCmd = scanner.nextLine().split(" ");

                    try {

                        // для команд без аргументов
                        if (parsedCmd.length == 1) {
                            commandResolver.getCommand(parsedCmd[0]).execute();
                        } else {
                            // с аргументами
                            commandResolver.getCommand(parsedCmd[0]).execute(parsedCmd);
                        }

                        if (parsedCmd[0].equalsIgnoreCase("exit")) break;
                    } catch (Exception e) {
                        System.out.println("Error while executing " + parsedCmd[0] + " occured: " + e.getMessage());
                    }
                }
            }

            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // хост и порт приходят из аргументов
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new Client(new ClientService(), new CommandResolver()).run(host, port);
    }
}
