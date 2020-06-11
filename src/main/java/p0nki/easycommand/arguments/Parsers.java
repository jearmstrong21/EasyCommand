package p0nki.easycommand.arguments;

import p0nki.easycommand.utils.Optional;

public class Parsers {

    public static final String GREEDY_STRING = "MODIFIER_GREEDY_STRING";
    public static final ParserFactory STRING = ParserFactory.name("STRING", argument -> {
        if (!CharSequence.class.isAssignableFrom(argument.getClazz())) return Optional.empty();
        if (argument.getModifiers().contains(GREEDY_STRING)) return Optional.of((source, reader) -> {
            String str = reader.readAll();
            if (str.equals("")) return Optional.empty();
            return Optional.of(str);
        });
        return Optional.of((source, reader) -> {
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
        });
    });
    public static final ParserFactory BOOLEAN = ParserFactory.name("BOOLEAN", argument -> {
        if (argument.getClazz() != Boolean.class) return Optional.empty();
        return Optional.of((source, reader) -> {
            String str = reader.readWord();
            if (str.equals("true")) return Optional.of(true);
            if (str.equals("false")) return Optional.of(false);
            return Optional.empty();
        });
    });
    public static final ParserFactory BYTE = ParserFactory.name("BYTE", argument -> {
        if (argument.getClazz() != Byte.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Byte.parseByte(reader.readWord())));
    });
    public static final ParserFactory CHAR = ParserFactory.name("CHAR", argument -> {
        if (argument.getClazz() != Character.class) return Optional.empty();
        return Optional.of((source, reader) -> {
            String str = reader.readWord();
            if (str.length() == 1) return Optional.of(str.charAt(0));
            return Optional.empty();
        });
    });
    public static final ParserFactory SHORT = ParserFactory.name("SHORT", argument -> {
        if (argument.getClazz() != Short.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Short.parseShort(reader.readWord())));
    });
    public static final ParserFactory INT = ParserFactory.name("INT", argument -> {
        if (argument.getClazz() != Integer.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Integer.parseInt(reader.readWord())));
    });
    public static final ParserFactory LONG = ParserFactory.name("LONG", argument -> {
        if (argument.getClazz() != Long.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Long.parseLong(reader.readWord())));
    });
    public static final ParserFactory FLOAT = ParserFactory.name("FLOAT", argument -> {
        if (argument.getClazz() != Float.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Float.parseFloat(reader.readWord())));
    });
    public static final ParserFactory DOUBLE = ParserFactory.name("DOUBLE", argument -> {
        if (argument.getClazz() != Double.class) return Optional.empty();
        return Optional.of((source, reader) -> Optional.emptyIfThrow(() -> Double.parseDouble(reader.readWord())));
    });

}
