package p0nki.easycommand;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Optional<T> {

    private final T value;

    private final boolean hasValue;

    private Optional(T value) {
        this.value = value;
        hasValue = true;
    }

    private Optional() {
        value = null;
        hasValue = false;
    }

    public static <T> Optional<T> emptyIfThrow(Callable<T> callable) {
        try {
            return of(callable.call());
        } catch (Exception e) {
            return empty();
        }
    }

    public static <T> Optional<T> empty() {
        return new Optional<>();
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    public T get() {
        if (!hasValue) throw new NoSuchElementException("No value present");
        return value;
    }

    public boolean isPresent() {
        return hasValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasValue, value); // This works since value is guaranteed to be null if hasValue is false so all empty optionals hash to the same value
    }

    public void ifPresent(Consumer<T> consumer) {
        if (hasValue) consumer.accept(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof Optional) {
            Optional<?> opt = (Optional<?>) other;
            return hasValue == opt.hasValue && Objects.equals(value, opt.value);
        }
        return false;
    }


}
