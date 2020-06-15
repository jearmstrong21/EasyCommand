package p0nki.easycommand;

import p0nki.easycommand.requirements.Requirement;
import p0nki.easycommand.utils.Optional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class RealCommand {

    private final Object cog;
    private final List<String[]> literals;
    private final List<String> names;
    private final List<Requirement> requirements;
    private final List<Requirement> cogRequirements;
    private final List<RealArgument> arguments;
    private final boolean takeSource;
    private final Method method;

    public RealCommand(Object cog, List<String[]> literals, List<String> names, List<Requirement> requirements, List<Requirement> cogRequirements, List<RealArgument> arguments, boolean takeSource, Method method) {
        this.cog = cog;
        this.literals = literals;
        this.names = names;
        this.requirements = requirements;
        this.cogRequirements = cogRequirements;
        this.arguments = arguments;
        this.takeSource = takeSource;
        this.method = method;
    }

    public boolean takeSource() {
        return takeSource;
    }

    public List<String[]> getLiterals() {
        return literals;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public List<Requirement> getCogRequirements() {
        return cogRequirements;
    }

    public List<RealArgument> getArguments() {
        return arguments;
    }

    public Method getMethod() {
        return method;
    }

    public Optional<String> rejectReason(Object source) {
        for (Requirement requirement : requirements) {
            Optional<String> reject = requirement.test(source);
            if (reject.isPresent()) return reject;
        }
        for (Requirement requirement : cogRequirements) {
            Optional<String> reject = requirement.test(source);
            if (reject.isPresent()) return reject;
        }
        return Optional.empty();
    }

    public void invoke(List<Object> arguments) {
        try {
            method.invoke(cog, arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
