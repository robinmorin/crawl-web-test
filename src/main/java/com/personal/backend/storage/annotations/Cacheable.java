package com.personal.backend.storage.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the method should be cached
 * Examples of usage:
 * P2D (2 days), PT2H (2 hours), PT2M (2 minutes), PT2S (2 seconds)
 * If not set, the cache will be stored indefinitely
  */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    String duration() default "";
}
