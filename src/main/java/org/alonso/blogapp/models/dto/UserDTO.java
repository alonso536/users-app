package org.alonso.blogapp.models.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String slug;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private String region;
    private String avatar;
    private Boolean active;
    private List<String> roles;
}
