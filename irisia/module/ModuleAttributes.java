package irisia.module;

;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleAttributes {
    String name();
    int keyBind() default 0;
    boolean toggled() default false;
    Module.Category category();
}