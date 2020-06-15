package p0nki.easycommand;

import p0nki.easycommand.annotations.*;
import p0nki.easycommand.arguments.ParserFactory;
import p0nki.easycommand.arguments.Parsers;
import p0nki.easycommand.requirements.Requirement;
import p0nki.easycommand.utils.Optional;

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

    public List<RealCommandCog> getCogs() {
        return cogs;
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

    public static boolean LOG_PARSE_ERRORS = false;

    public RealCommandCog createCog(Object cog, Class<?> clazz) {
        if (clazz.isAnnotationPresent(CommandCog.class)) {
            CommandCog commandCog = clazz.getAnnotation(CommandCog.class);
            Method[] methods = clazz.getMethods();
            List<RealCommand> commands = new ArrayList<>();
            List<Requirement> cogRequirements = Arrays.stream(commandCog.requirements()).map(aClass -> {
                try {
                    return aClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            for (Method method : methods) {
                if (method.isAnnotationPresent(Command.class)) {
                    Command command = method.getAnnotation(Command.class);
                    String[] names = command.names();
                    if (names.length == 0) names = new String[]{method.getName()};
                    List<RealArgument> args = new ArrayList<>();
                    List<Parameter> parameters = new ArrayList<>(Arrays.asList(method.getParameters()));
                    boolean takeSource = false;
                    if (parameters.size() > 0) {
                        if (parameters.get(0).isAnnotationPresent(Source.class)) {
                            if (parameters.get(0).isAnnotationPresent(Argument.class))
                                throw new IllegalArgumentException("Source and Argument cannot be present on the same parameter");
                            parameters.remove(0);
                            takeSource = true;
                        }
                        args = parameters.stream().map(parameter -> {
                            if (parameter.isAnnotationPresent(Argument.class)) {
                                return new RealArgument(parameter, parsers);
                            } else {
                                throw new IllegalArgumentException("All parameters must be annotated with @Argument");
                            }
                        }).collect(Collectors.toList());
                    }
                    List<Requirement> requirements = Arrays.stream(command.requirements()).map(requirementClazz -> {
                        try {
                            return requirementClazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
                    commands.add(new RealCommand(cog,
                            Arrays.stream(command.literals()).map(Literal::value).collect(Collectors.toList()),
                            Arrays.asList(names), requirements, cogRequirements, args, takeSource, method));
                }
            }
            RealCommandCog realCog = new RealCommandCog(commandCog.name(), commands);
            cogs.add(realCog);
            return realCog;
        } else {
            throw new IllegalArgumentException("No command cog");
        }
    }

    private RunAttemptResult attemptRun(Object source, String str, RealCommand command) {
        CommandReader reader = new CommandReader(str);
        for (String[] literal : command.getLiterals()) {
            String next = reader.readWord();
            if (!Arrays.asList(literal).contains(next)) {
                return new RunAttemptResult(false, Optional.of("Unexpected literal " + next), Optional.empty());
            }
        }
        if (command.getNames().contains(reader.readWord())) {
            List<Object> arguments = new ArrayList<>();
            if (command.takeSource()) arguments.add(source);
            for (RealArgument arg : command.getArguments()) {
                Optional<?> obj = arg.parse(source, reader);
                if (obj.isPresent()) {
                    arguments.add(obj.get());
                } else {
                    return new RunAttemptResult(false, Optional.of("Failed to parse argument " + arg.getName()), Optional.empty());
                }
            }
            if (reader.getIndex() != str.length())
                return new RunAttemptResult(false, Optional.of("Unconsumed tokens"), Optional.empty());
            Optional<String> rejectReason = command.rejectReason(source);
            if (rejectReason.isPresent()) return new RunAttemptResult(true, Optional.empty(), rejectReason);
            command.invoke(arguments);
            return new RunAttemptResult(true, Optional.empty(), Optional.empty());
        }
        return new RunAttemptResult(false, Optional.of("No command name found"), Optional.empty());
    }

    public Optional<String> run(Object source, String str) {
        for (RealCommandCog cog : cogs) {
            for (RealCommand command : cog.getCommands()) {
                RunAttemptResult res = attemptRun(source, str, command);
                if (res.requirementError.isPresent()) {
                    return res.requirementError;
                } else if (res.parseError.isPresent()) {
                    if (LOG_PARSE_ERRORS) res.parseError.ifPresent(System.out::println);
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.of("No command found");
    }

    private static class RunAttemptResult {

        public final boolean parsed;
        public final Optional<String> parseError;
        public final Optional<String> requirementError;

        public RunAttemptResult(boolean parsed, Optional<String> parseError, Optional<String> requirementError) {
            this.parsed = parsed;
            this.parseError = parseError;
            this.requirementError = requirementError;
        }

    }

}
