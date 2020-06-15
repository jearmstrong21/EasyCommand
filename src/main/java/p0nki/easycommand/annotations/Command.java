package p0nki.easycommand.annotations;

import p0nki.easycommand.requirements.Requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    Literal[] literals() default {};

    String[] names() default {};

    Class<? extends Requirement>[] requirements() default {};

}
