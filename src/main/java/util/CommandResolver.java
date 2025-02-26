package util;

import client.cmd.*;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandResolver {
    private final Map<String, CommandLine> commands = new HashMap<>();

    {
        commands.put(getCommandName(ExitCommand.class), new CommandLine(new ExitCommand()));
        commands.put(getCommandName(LoginCommand.class), new CommandLine(new LoginCommand()));
        commands.put(getCommandName(DefaultCommand.class), new CommandLine(new DefaultCommand()));
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

    public CommandLine getCommand(String cmd) {
        if (commands.containsKey(cmd)) {
            return commands.get(cmd);
        }
        return commands.get("default"); // если пользователь ввел несуществующую команду
    }
}
