package org.wahlzeit.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Repeating PatternInstance annotations.
 * https://docs.oracle.com/javase/tutorial/java/annotations/repeating.html
 */
@Target(ElementType.TYPE) // Annotation for classes
public @interface PatternInstances {
    PatternInstance[] value();
}
