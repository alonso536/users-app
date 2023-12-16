package org.alonso.blogapp.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    @NotBlank
    private String to;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
