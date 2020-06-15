package p0nki.easycommand.requirements;

import p0nki.easycommand.utils.Optional;

public interface Requirement {

    Optional<String> test(Object source);

}
