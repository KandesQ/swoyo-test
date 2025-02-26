package util;

import client.cmd.*;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.HashMap;
import java.util.Map;

public class CommandResolver {
    private static final Map<String, Runnable> commands = new HashMap<>();

    {
        commands.put(getCommandName(ExitCommand.class), new ExitCommand());
        commands.put(getCommandName(LoginCommand.class), new LoginCommand());
        commands.put(getCommandName(DefaultCommand.class), new DefaultCommand());
        commands.put(getCommandName(ViewCommand.class), new ViewCommand());
    }

    /**
     * Позволяет получить имя команды из аннотации через рефлексию
     *
     * Если пользователь передал неверный класс или у класс - не команда, то вернется команда по умолчанию
     */
    public static String getCommandName(Class<?> commandClass) {
        Command cmdAnnotation = commandClass.getAnnotation(Command.class);
        if (cmdAnnotation != null) {
            return cmdAnnotation.name();
        }
        return "default";
    }

    public static CommandLine getCommandLine(String cmd) {
        if (commands.containsKey(cmd)) {
            return new CommandLine(commands.get(cmd));
        }
        return new CommandLine(commands.get("default")); // если пользователь ввел несуществующую команду
    }

    public static Runnable getCommand(String cmd) {
        if (commands.containsKey(cmd)) {
            return commands.get(cmd);
        }

        return commands.get("default");
    }
}
