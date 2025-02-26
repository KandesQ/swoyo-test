package client.cmd;

import picocli.CommandLine;
import picocli.CommandLine.*;

import java.sql.SQLOutput;
import java.util.concurrent.Callable;

@Command(name = "login", description = "user Authorization")
public class LoginCommand implements Callable<String> {

    @Option(names = {"-u", "--username"}, description = "username", required = true)
    private String username;

    // пустой конструктор для рефлексии по пакету
    public LoginCommand() {}

    @Override
    public String call() throws Exception {
        // логика авторизации...
        System.out.println("user " + username + " authorized. Welcome to the Vote App!");
        return username;
    }
}
