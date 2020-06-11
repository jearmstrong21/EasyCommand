package p0nki.easycommand;

import p0nki.easycommand.arguments.ArgumentParser;
import p0nki.easycommand.arguments.ParserFactory;

public class ReturnSourceParser implements ParserFactory {
    @Override
    public Optional<ArgumentParser> create(RealArgument argument) {
        return Optional.of((source, reader) -> Optional.of(source));
    }
}
