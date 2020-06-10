package p0nki.easycommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class RealCommand {

    private final Object cog;
    private final List<String[]> literals;
    private final List<String> names;
    private final List<RealArgument> arguments;
    private final Method method;

    public RealCommand(Object cog, List<String[]> literals, List<String> names, List<RealArgument> arguments, Method method) {
        this.cog = cog;
        this.literals = literals;
        this.names = names;
        this.arguments = arguments;
        this.method = method;
    }

    public List<String[]> getLiterals() {
        return literals;
    }

    public List<String> getNames() {
        return names;
    }

    public List<RealArgument> getArguments() {
        return arguments;
    }

    public void invoke(List<Object> arguments) {
        try {
            method.invoke(cog, arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
