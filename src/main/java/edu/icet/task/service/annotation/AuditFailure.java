package edu.icet.task.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Can be applied to methods [cite: 42]
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditFailure {
}
