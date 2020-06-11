package p0nki.easycommand.arguments;

import p0nki.easycommand.CommandReader;
import p0nki.easycommand.Optional;

@FunctionalInterface
public interface ArgumentParser {

    Optional<?> parse(Object source, CommandReader reader);

}
