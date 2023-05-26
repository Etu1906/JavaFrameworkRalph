package model;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // durée de vie ( execution )
@Target(ElementType.METHOD)  //ne peut être utilisée que sur des methodes de classe.
public @interface Argument{
    String[] argument();
}
