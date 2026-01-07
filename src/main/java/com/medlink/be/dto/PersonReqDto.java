package com.medlink.be.dto;

import com.medlink.be.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record PersonReqDto(
    @NotBlank(message = "{error.person.first-name.empty}")
    String firstName,

    @NotBlank(message = "{error.person.last-name.empty}")
    String lastName,

    @NotNull(message = "{error.person.dob.empty}")
    LocalDate dateOfBirth,

    @NotNull(message = "{error.person.gender.empty}")
    Gender gender,

    @NotBlank(message = "{error.person.phone.empty}")
    @Pattern(
        regexp = "^\\+[1-9]\\d{7,14}$",
        message = "{error.person.phone.invalid}"
    )
    String phone,

    @NotBlank(message = "{error.person.address.empty}")
    String address,

    @NotBlank(message = "{error.person.suburb.empty}")
    String suburb,

    @NotBlank(message = "{error.person.state.empty}")
    String state,

    @NotBlank(message = "{error.person.postcode.empty}")
    @Pattern(
        regexp = "^\\d{1,4}$",
        message = "{error.person.postcode.invalid}"
    )
    String postcode
) {

}
