package p0nki.easycommand.arguments;

import p0nki.easycommand.CommandReader;
import p0nki.easycommand.Optional;
import p0nki.easycommand.annotations.Argument;

import java.util.function.Function;

public class PrimitiveParserFactory {

    public static final String MODIFIER_GREEDY_STRING = "MODIFIER_GREEDY_STRING";
    public static final ArgumentParserFactory STRING = new ArgumentParserFactory() {
        @Override
        public boolean canParse(Argument argument, Class<?> clazz) {
            return String.class.isAssignableFrom(clazz);
        }

        @Override
        public ArgumentParser create(Argument argument, Class<?> clazz) {
            for (String modifier : argument.modifiers()) {
                if (modifier.equals(MODIFIER_GREEDY_STRING)) return reader -> {
                    String str = reader.readAll();
                    if (str.equals("")) return Optional.empty();
                    return Optional.of(str);
                };
            }
            return reader -> {
                if (!reader.canRead()) return Optional.empty();
                char first = reader.next();
                StringBuilder str = new StringBuilder();
                if (first == '"') {
                    char ch;
                    while (reader.canRead() && first != (ch = reader.next())) {
                        str.append(ch);
                    }
                    if (reader.canRead()) reader.next();
                } else {
                    str.append(first).append(reader.readWord());
                }
                if (str.toString().equals("")) return Optional.empty();
                return Optional.of(str.toString());
            };
        }
    };
    private static final Function<CommandReader, Object> BOOLEAN_PARSER = reader -> {
        String str = reader.readWord();
        if (str.equals("true")) return Optional.of(true);
        if (str.equals("false")) return Optional.of(false);
        return Optional.empty();
    };
    public static final ArgumentParserFactory BOOLEAN = factory(boolean.class, Boolean.class, BOOLEAN_PARSER);
    private static final Function<CommandReader, Object> BYTE_PARSER = reader -> Byte.parseByte(reader.readWord());
    public static final ArgumentParserFactory BYTE = factory(byte.class, Byte.class, BYTE_PARSER);
    private static final Function<CommandReader, Object> CHAR_PARSER = reader -> {
        String str = reader.readWord();
        if (str.length() == 1) return Optional.of(str.charAt(0));
        return Optional.empty();
    };
    public static final ArgumentParserFactory CHAR = factory(char.class, Character.class, CHAR_PARSER);
    private static final Function<CommandReader, Object> SHORT_PARSER = reader -> Short.parseShort(reader.readWord());
    public static final ArgumentParserFactory SHORT = factory(short.class, Short.class, SHORT_PARSER);
    private static final Function<CommandReader, Object> INT_PARSER = reader -> Integer.parseInt(reader.readWord());
    public static final ArgumentParserFactory INT = factory(int.class, Integer.class, INT_PARSER);
    private static final Function<CommandReader, Object> LONG_PARSER = reader -> Long.parseLong(reader.readWord());
    public static final ArgumentParserFactory LONG = factory(long.class, Long.class, LONG_PARSER);
    private static final Function<CommandReader, Object> FLOAT_PARSER = reader -> Float.parseFloat(reader.readWord());
    public static final ArgumentParserFactory FLOAT = factory(float.class, Float.class, FLOAT_PARSER);
    private static final Function<CommandReader, Object> DOUBLE_PARSER = reader -> Double.parseDouble(reader.readWord());
    public static final ArgumentParserFactory DOUBLE = factory(double.class, Double.class, DOUBLE_PARSER);

    private static ArgumentParserFactory factory(Class<?> primitive, Class<?> boxed, Function<CommandReader, Object> parser) {
        return new ArgumentParserFactory() {
            @Override
            public boolean canParse(Argument argument, Class<?> clazz) {
                return primitive.isAssignableFrom(clazz) || boxed.isAssignableFrom(clazz);
            }

            @Override
            public ArgumentParser create(Argument argument, Class<?> clazz) {
                return reader -> Optional.emptyIfThrow(() -> parser.apply(reader)); // wrap
            }
        };
    }

}
