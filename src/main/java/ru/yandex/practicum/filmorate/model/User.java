package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class User extends BaseUnit {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "В логине не может быть пробелов")
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Long> friends = new TreeSet<>();


}
