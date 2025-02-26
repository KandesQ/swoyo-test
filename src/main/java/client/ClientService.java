package client;

import client.cmd.LoginCommand;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import picocli.CommandLine;

public class ClientService {
    // перенести логику из CommandResolver
    // все crud методы должны отправлять dto, а с помощью equals() hashCode() будет изменяться data в ServerService


}
