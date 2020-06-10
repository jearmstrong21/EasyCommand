package p0nki.easycommand.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Argument {

    String name();

    int minimumCount() default 1;

    int maximumCount() default Integer.MAX_VALUE;

    String[] modifiers() default {};

    Class<?> parse() default Void.class;

}
