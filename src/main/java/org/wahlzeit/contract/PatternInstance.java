package org.wahlzeit.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Annotation for classes
@Repeatable(PatternInstances.class)
public @interface PatternInstance {
    String patternName();
    Class[] participants();
    String description() default "" ;
}
