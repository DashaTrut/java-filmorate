package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Film extends BaseUnit {

    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration_min; //    продолжительность фильма


//    @Validated
//    @Retention(RetentionPolicy.RUNTIME)
//    @Constraint(validatedBy = MinimumDateValidator.class)
//    @Past
//    public @interface MinimumDate {
//        String message() default "Date must not be before {value}";
//
//        Class<?>[] groups() default {};
//
//        Class<?>[] payload() default {};
//
//        String value() default "1895-12-28";
//    }
//
//    public class MinimumDateValidator implements ConstraintValidator<MinimumDate, LocalDate> {
//        private LocalDate minimumDate;
//
//        @Override
//        public void initialize(MinimumDate constraintAnnotation) {
//            minimumDate = LocalDate.parse(constraintAnnotation.value());
//        }
//
//        @Override
//        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
//            return value == null || !value.isBefore(minimumDate);
//        }
//    }

}
