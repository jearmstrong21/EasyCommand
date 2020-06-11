package p0nki.easycommand.arguments;

import p0nki.easycommand.RealArgument;
import p0nki.easycommand.utils.Optional;

import java.util.function.Function;

@FunctionalInterface
public interface ParserFactory {

    static Named name(String name, Function<RealArgument, Optional<ArgumentParser>> function) {
        return new NamedImpl(name, function);
    }

    Optional<ArgumentParser> create(RealArgument argument);

    interface Named extends ParserFactory {

        String name();

    }

//    static Named name(String name, ParserFactory factory) {
//        return new Named() {
//            @Override
//            public Optional<ArgumentParser> create(RealArgument argument) {
//                return factory.create(argument);
//            }
//
//            @Override
//            public String name() {
//                return name;
//            }
//        };
//    }

    final class NamedImpl implements Named {

        private final String name;
        private final Function<RealArgument, Optional<ArgumentParser>> function;

        public NamedImpl(String name, Function<RealArgument, Optional<ArgumentParser>> function) {
            this.name = name;
            this.function = function;
        }

        @Override
        public Optional<ArgumentParser> create(RealArgument argument) {
            return function.apply(argument);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return "PARSER[" + name + "]";
        }
    }

}
