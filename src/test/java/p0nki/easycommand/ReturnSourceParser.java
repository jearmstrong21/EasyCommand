package p0nki.easycommand;

import p0nki.easycommand.arguments.ArgumentParser;
import p0nki.easycommand.arguments.ParserFactory;
import p0nki.easycommand.utils.Optional;

public class ReturnSourceParser implements ParserFactory {
    @Override
    public Optional<ArgumentParser> create(RealArgument argument) {
        return Optional.of((source, reader) -> Optional.of(source));
    }
}
