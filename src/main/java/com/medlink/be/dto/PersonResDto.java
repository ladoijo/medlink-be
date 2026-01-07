package com.medlink.be.dto;

import com.medlink.be.constant.Gender;
import com.medlink.be.entity.Person;
import java.time.LocalDate;

public record PersonResDto(
    long id,
    String pid,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    Gender gender,
    String phone,
    String address,
    String suburb,
    String state,
    String postcode
) {

  public PersonResDto(Person person) {
    this(person.getId(), person.getPid(), person.getFirstName(), person.getLastName(),
        person.getDateOfBirth(), person.getGender(), person.getPhone(), person.getAddress(),
        person.getSuburb(), person.getState(), person.getPostcode());
  }
}
