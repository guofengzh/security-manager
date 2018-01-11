package avicit.plm.core.accesscontrol.dispatcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfPackageDescription {
    // the description about the package
    String value() default "" ;
}
