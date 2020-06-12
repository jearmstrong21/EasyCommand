package p0nki.easycommand.arguments;

import p0nki.easycommand.utils.Optional;

public class Parsers {

    // --- MODIFIERS ---
    public static final String GREEDY_STRING = "MODIFIER_GREEDY_STRING";

    // --- PARSERS ---
    public static final ArgumentParser GREEDY_STRING_PARSER = (source, reader) -> {
        String str = reader.readAll();
        if (str.equals("")) return Optional.empty();
        return Optional.of(str);
    };

    public static final ArgumentParser STRING_PARSER = (source, reader) -> {
        if (!reader.canRead()) return Optional.empty();
        char first = reader.next();
        StringBuilder str = new StringBuilder();
        if (first == '"' || first == '\'') {
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

    public static final ArgumentParser BOOLEAN_PARSER = (source, reader) -> {
        String str = reader.readWord();
        if (str.equals("true")) return Optional.of(true);
        if (str.equals("false")) return Optional.of(false);
        return Optional.empty();
    };

    public static final ArgumentParser BYTE_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Byte.parseByte(reader.readWord()));
    public static final ArgumentParser CHAR_PARSER = (source, reader) -> {
        String str = reader.readWord();
        if (str.length() == 1) return Optional.of(str.charAt(0));
        return Optional.empty();
    };
    public static final ArgumentParser SHORT_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Short.parseShort(reader.readWord()));
    public static final ArgumentParser INT_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Integer.parseInt(reader.readWord()));
    public static final ArgumentParser LONG_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Long.parseLong(reader.readWord()));
    public static final ArgumentParser FLOAT_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Float.parseFloat(reader.readWord()));
    public static final ArgumentParser DOUBLE_PARSER = (source, reader) -> Optional.emptyIfThrow(() -> Double.parseDouble(reader.readWord()));

    // --- FACTORIES ---
    public static final ParserFactory STRING = ParserFactory.name("STRING", argument -> {
        if (!CharSequence.class.isAssignableFrom(argument.getClazz())) return Optional.empty();
        if (argument.getModifiers().contains(GREEDY_STRING)) return Optional.of(GREEDY_STRING_PARSER);
        return Optional.of(STRING_PARSER);
    });
    public static final ParserFactory BOOLEAN = ParserFactory.name("BOOLEAN", argument -> {
        if (argument.getClazz() != Boolean.class) return Optional.empty();
        return Optional.of(BOOLEAN_PARSER);
    });
    public static final ParserFactory BYTE = ParserFactory.name("BYTE", argument -> {
        if (argument.getClazz() != Byte.class) return Optional.empty();
        return Optional.of(BYTE_PARSER);
    });
    public static final ParserFactory CHAR = ParserFactory.name("CHAR", argument -> {
        if (argument.getClazz() != Character.class) return Optional.empty();
        return Optional.of(CHAR_PARSER);
    });
    public static final ParserFactory SHORT = ParserFactory.name("SHORT", argument -> {
        if (argument.getClazz() != Short.class) return Optional.empty();
        return Optional.of(SHORT_PARSER);
    });
    public static final ParserFactory INT = ParserFactory.name("INT", argument -> {
        if (argument.getClazz() != Integer.class) return Optional.empty();
        return Optional.of(INT_PARSER);
    });
    public static final ParserFactory LONG = ParserFactory.name("LONG", argument -> {
        if (argument.getClazz() != Long.class) return Optional.empty();
        return Optional.of(LONG_PARSER);
    });
    public static final ParserFactory FLOAT = ParserFactory.name("FLOAT", argument -> {
        if (argument.getClazz() != Float.class) return Optional.empty();
        return Optional.of(FLOAT_PARSER);
    });
    public static final ParserFactory DOUBLE = ParserFactory.name("DOUBLE", argument -> {
        if (argument.getClazz() != Double.class) return Optional.empty();
        return Optional.of(DOUBLE_PARSER);
    });

}
