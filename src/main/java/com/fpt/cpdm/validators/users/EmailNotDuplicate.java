package com.fpt.cpdm.validators.users;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailNotDuplicateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotDuplicate {

    String message() default "This email '${validatedValue}' is already existed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
