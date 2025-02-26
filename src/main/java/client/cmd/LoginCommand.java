package client.cmd;

import client.Client;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.sql.SQLOutput;
import java.util.concurrent.Callable;

@Command(name = "login", description = "user Authorization")
public class LoginCommand implements Runnable {

    @Option(names = {"-u"}, description = "username", required = true)
    private String username;

    @Override
    public void run() {
        // простая логика авторизации...
        Client.username = username;

        System.out.println("User " + username + " authorized. Welcome to the Vote App!");
    }
}
