package p0nki.easycommand;

import p0nki.easycommand.annotations.*;
import p0nki.easycommand.arguments.ParserFactory;
import p0nki.easycommand.arguments.Parsers;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDispatcher {

    private final List<ParserFactory> parsers;
    private final List<RealCommandCog> cogs;

    public CommandDispatcher() {
        parsers = new ArrayList<>();
        cogs = new ArrayList<>();
    }

    public void addParser(ParserFactory parser) {
        parsers.add(parser);
    }

    public void addPrimitives() {
        addParser(Parsers.BOOLEAN);
        addParser(Parsers.BYTE);
        addParser(Parsers.CHAR);
        addParser(Parsers.SHORT);
        addParser(Parsers.INT);
        addParser(Parsers.LONG);
        addParser(Parsers.FLOAT);
        addParser(Parsers.DOUBLE);
        addParser(Parsers.STRING);
    }

    public RealCommandCog createCog(Object cog, Class<?> clazz) {
        if (clazz.isAnnotationPresent(CommandCog.class)) {
            CommandCog commandCog = clazz.getAnnotation(CommandCog.class);
            Method[] methods = clazz.getMethods();
            List<RealCommand> commands = new ArrayList<>();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Command.class)) {
                    Command command = method.getAnnotation(Command.class);
                    String[] names = command.names();
                    if (names.length == 0) names = new String[]{method.getName()};
                    List<RealArgument> args = new ArrayList<>();
                    List<Parameter> parameters = new ArrayList<>(Arrays.asList(method.getParameters()));
                    if (parameters.size() > 0) {
                        if (parameters.get(0).isAnnotationPresent(Source.class)) {
                            parameters.remove(0);
                        }
                        args = parameters.stream().map(parameter -> {
                            if (parameter.isAnnotationPresent(Argument.class)) {
                                return new RealArgument(parameter, parsers);
                            } else {
                                throw new IllegalArgumentException("All parameters must be annotated with @Argument");
                            }
                        }).collect(Collectors.toList());
                    }
                    commands.add(new RealCommand(cog, Arrays.stream(command.literals()).map(Literal::value).collect(Collectors.toList()), Arrays.asList(names), args, method));
                }
            }
            RealCommandCog realCog = new RealCommandCog(commandCog.value(), commands);
            cogs.add(realCog);
            return realCog;
        } else {
            throw new IllegalArgumentException("No command cog");
        }
    }

    private Optional<String> attemptRun(Optional<Object> source, String str, RealCommand command) {
        CommandReader reader = new CommandReader(str);
        for (String[] literal : command.getLiterals()) {
            String next = reader.readWord();
            if (!Arrays.asList(literal).contains(next)) {
                return Optional.of("Unexpected literal " + next);
            }
        }
        if (command.getNames().contains(reader.readWord())) {
            List<Object> arguments = new ArrayList<>();
            source.ifPresent(arguments::add);
            for (RealArgument arg : command.getArguments()) {
                Optional<?> obj = arg.parse(reader);
                if (obj.isPresent()) {
                    arguments.add(obj.get());
                } else {
                    return Optional.of("Failed to parse argument " + arg.getName());
                }
            }
            command.invoke(arguments);
            if (reader.getIndex() != str.length()) return Optional.of("Unconsumed tokens");
            return Optional.empty();
        }
        return Optional.of("No command name found");
    }

    public void run(Optional<Object> source, String str) {
        for (RealCommandCog cog : cogs) {
            for (RealCommand command : cog.getCommands()) {
                Optional<String> res = attemptRun(source, str, command);
                if (!res.isPresent()) {
                    return;
                }
//                else {
//                    System.out.println(res.get());
//                }
            }
        }
        throw new RuntimeException("No command found: " + str);
    }

}
