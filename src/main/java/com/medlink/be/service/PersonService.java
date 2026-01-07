package com.medlink.be.service;

import com.medlink.be.dto.PaginatedDto;
import com.medlink.be.dto.PersonReqDto;
import com.medlink.be.dto.PersonResDto;
import com.medlink.be.entity.Person;
import com.medlink.be.exception.ResourceNotFoundException;
import com.medlink.be.repository.PersonRepo;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepo repo;

  public PaginatedDto<PersonResDto> findAll(@Nullable String pid, @Nullable String firstName,
      @Nullable String lastName, int page,
      int size) {
    var p = Math.max(page - 1, 0);
    var s = size <= 0 ? 10 : Math.min(size, 100);

    Specification<Person> spec = (root, query, cb) -> cb.conjunction();

    if (StringUtils.hasText(pid)) {
      var v = pid.trim();
      spec = spec.and((root, query, cb) -> cb.equal(root.get("pid"), v));
    }

    if (StringUtils.hasText(firstName)) {
      var v = "%" + firstName.trim().toLowerCase() + "%";
      spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("firstName")), v));
    }

    if (StringUtils.hasText(lastName)) {
      var v = "%" + lastName.trim().toLowerCase() + "%";
      spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("lastName")), v));
    }

    var pageable = PageRequest.of(p, s);
    var resultPage = repo.findAll(spec, pageable);
    return PaginatedDto.from(resultPage, PersonResDto::new);
  }

  public PersonResDto findById(Long id) {
    var person = repo.findById(id).orElse(null);
    return person == null ? null : new PersonResDto(person);
  }

  public PersonResDto create(PersonReqDto reqDto) {
    var person = new Person(reqDto);
    person = repo.save(person);
    return new PersonResDto(person);
  }

  public PersonResDto update(long id, PersonReqDto reqDto) {
    var person = repo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("error.person.not-found"));

    person.setFirstName(reqDto.firstName());
    person.setLastName(reqDto.lastName());
    person.setDateOfBirth(reqDto.dateOfBirth());
    person.setGender(reqDto.gender());
    person.setPhone(reqDto.phone());
    person.setAddress(reqDto.address());
    person.setSuburb(reqDto.suburb());
    person.setState(reqDto.state());
    person.setPostcode(reqDto.postcode());
    person = repo.save(person);
    return new PersonResDto(person);
  }

  @Transactional
  public void deleteFlagById(Long id) {
    repo.deleteFlagById(id);
  }
}
