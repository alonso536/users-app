package org.alonso.blogapp.models.dto.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class Validator {

    public static List<String> validate(BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors()
                    .stream()
                    .map((error) -> "El campo " + error.getField() + " " + error.getDefaultMessage())
                    .collect(Collectors.toList());
        }

        return new ArrayList<String>();
    }
}
