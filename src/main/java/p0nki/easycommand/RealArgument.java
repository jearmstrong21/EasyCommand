package p0nki.easycommand;

import javafx.util.Pair;
import p0nki.easycommand.annotations.Argument;
import p0nki.easycommand.arguments.ArgumentParser;
import p0nki.easycommand.arguments.ParserFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RealArgument {

    private final String name;
    private final Optional<Pair<Integer, Integer>> count;
    private final List<String> modifiers;
    private final Class<?> clazz;
    private final ArgumentParser parser;

    public RealArgument(Parameter parameter, List<ParserFactory> factories) {
        Argument argument = parameter.getAnnotation(Argument.class);
        name = argument.name();
        clazz = boxClass(parameter.getType().isArray() ? parameter.getType().getComponentType() : parameter.getType());
        count = parameter.getType().isArray() ? Optional.of(new Pair<>(argument.minimumCount(), argument.maximumCount())) : Optional.empty();
        modifiers = Arrays.asList(argument.modifiers());
        if (argument.parse() != Void.class) {
            try {
                Optional<ArgumentParser> parserOptional = ((ParserFactory) argument.parse().getConstructor().newInstance()).create(this);
                if (!parserOptional.isPresent()) {
                    throw new IllegalArgumentException("Specified parser cannot parse this argument");
                } else {
                    parser = parserOptional.get();
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new IllegalArgumentException("Failed to instantiate explicit argument parser: " + argument.parse().toString(), e);
            }
        } else {
            parser = findFactory(factories);
        }
    }

    public static Class<?> boxClass(Class<?> clazz) {
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == short.class) return Short.class;
        if (clazz == char.class) return Character.class;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == float.class) return Float.class;
        if (clazz == double.class) return Double.class;
        return clazz;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    private ArgumentParser findFactory(List<ParserFactory> factories) {
        for (ParserFactory factory : factories) {
            Optional<ArgumentParser> parserOptional = factory.create(this);
            if (parserOptional.isPresent()) {
                return parserOptional.get();
            }
        }
        throw new IllegalArgumentException("Cannot find factory for " + clazz.toString());
    }

    public String getName() {
        return name;
    }

    public Optional<?> parse(CommandReader reader) {
        if (count.isPresent()) {
            List<Object> values = new ArrayList<>();
            Optional<?> optional;
            while ((optional = parser.parse(reader)).isPresent()) {
                values.add(optional.get());
            }
            if (count.get().getKey() > values.size() || values.size() > count.get().getValue()) return Optional.empty();
            if (values.size() == 0) return Optional.empty();
            Object array = Array.newInstance(clazz, values.size());
            for (int i = 0; i < values.size(); i++) {
                Array.set(array, i, values.get(i));
            }
            return Optional.of(array);
        }
        return parser.parse(reader);
    }

}
