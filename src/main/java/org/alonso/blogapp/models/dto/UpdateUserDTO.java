package org.alonso.blogapp.models.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @NotBlank
    @Size(min = 3, max = 30)
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 30)
    private String lastname;

    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String slug;

    @NotBlank
    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[0-9]{7,15}$", message = "debe ser un número")
    private String phone;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @Positive
    private Long region;
}
