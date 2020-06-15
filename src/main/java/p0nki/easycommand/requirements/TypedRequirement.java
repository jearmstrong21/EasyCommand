package p0nki.easycommand.requirements;

import p0nki.easycommand.utils.Optional;

public abstract class TypedRequirement<S> implements Requirement {

    private final Class<S> clazz;

    protected TypedRequirement(Class<S> clazz) {
        this.clazz = clazz;
    }

    protected abstract Optional<String> testType(S source);

    @Override
    public final Optional<String> test(Object source) {
        if (clazz.isInstance(source)) return testType(clazz.cast(source));
        return Optional.of("Incorrect source type. Expected " + clazz.toString() + ", got " + source.getClass().toString());
    }
}
