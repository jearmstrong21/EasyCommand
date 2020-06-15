package p0nki.easycommand;

import p0nki.easycommand.requirements.TypedRequirement;
import p0nki.easycommand.utils.Optional;

public class IntValueRequirement extends TypedRequirement<Integer> {

    public IntValueRequirement() {
        super(Integer.class);
    }

    @Override
    protected Optional<String> testType(Integer source) {
        if (source == 5) return Optional.empty();
        return Optional.of("Expected 5, got " + source);
    }
}
