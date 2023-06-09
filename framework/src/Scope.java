package model;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // durée de vie ( execution )
@Target(ElementType.TYPE)  //ne peut être utilisée que sur des champs de classe.
public @interface Scope{
    String name() default "";
}
