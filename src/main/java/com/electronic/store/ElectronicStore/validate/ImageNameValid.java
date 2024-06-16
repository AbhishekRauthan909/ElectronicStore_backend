package com.electronic.store.ElectronicStore.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid
{
   //This is for error message
    String message() default "Invalid Image Name!!!";

    //This represent group of constraints
    Class<?>[] groups() default{};

    //This give some additional information about constraints
    Class<? extends Payload>[] payload() default {};
}
