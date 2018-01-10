package avicit.plm.core.accesscontrol.dispatcher;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RunIt {
    /** a regression to match the input */
    String value() default "GET" ;
}
