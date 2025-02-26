package client.cmd;

import picocli.CommandLine;

@CommandLine.Command(name = "default", description = "если юзер ввел что-то незнакомое")
public class DefaultCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Unknown command, please, check command syntax");
    }
}
