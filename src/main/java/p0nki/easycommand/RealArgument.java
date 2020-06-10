package p0nki.easycommand;

import javafx.util.Pair;
import p0nki.easycommand.annotations.Argument;
import p0nki.easycommand.arguments.ArgumentParser;
import p0nki.easycommand.arguments.ArgumentParserFactory;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RealArgument {

    private final String name;
    private final Optional<Pair<Integer, Integer>> count;
    private final ArgumentParser parser;

    public RealArgument(Parameter parameter, List<ArgumentParserFactory> factories) {
        Argument argument = parameter.getAnnotation(Argument.class);
        name = argument.name();
        Class<?> clazz = parameter.getType();
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
            count = Optional.of(new Pair<>(argument.minimumCount(), argument.maximumCount()));
        } else {
            count = Optional.empty();
        }
        if (argument.parse() != Void.class) {
            try {
                parser = (ArgumentParser) argument.parse().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
                throw new IllegalArgumentException("Failed to instantiate explicit argument parser: " + argument.parse().toString());
            }
        } else {
            parser = findFactory(argument, clazz, factories).create(argument, clazz);
        }
    }

    private static ArgumentParserFactory findFactory(Argument argument, Class<?> clazz, List<ArgumentParserFactory> factories) {
        for (ArgumentParserFactory factory : factories) {
            if (factory.canParse(argument, clazz)) return factory;
        }
        throw new IllegalArgumentException("Cannot find factory for " + clazz.toString());
    }

    public String getName() {
        return name;
    }

    public Optional<Object> parse(CommandReader reader) {
        if (count.isPresent()) {
            List<Object> values = new ArrayList<>();
            Optional<Object> optional;
            while ((optional = parser.parse(reader)).isPresent()) {
                values.add(optional.get());
            }
            if (values.size() == 0) return Optional.empty();
            return Optional.of(values.toArray());
        }
        return parser.parse(reader);
    }

}
