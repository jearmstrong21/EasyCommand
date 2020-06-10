package p0nki.easycommand.arguments;

import p0nki.easycommand.annotations.Argument;

public interface ArgumentParserFactory {

    boolean canParse(Argument argument, Class<?> clazz);

    ArgumentParser create(Argument argument, Class<?> clazz);

}
