package org.wahlzeit.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

//@Target(ElementType.METHOD) // Annotation for methods
public @interface PatternMethod {
    String patternName();
    String description() default "" ;
}
