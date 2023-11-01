package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
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
    private String login;
    private String name; //    имя для отображения
    @PastOrPresent
    private LocalDate birthday;

    private Set<Long> friends = new TreeSet<>();
}
