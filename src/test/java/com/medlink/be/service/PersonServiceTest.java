package com.medlink.be.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.medlink.be.constant.Gender;
import com.medlink.be.dto.PaginatedDto;
import com.medlink.be.dto.PersonReqDto;
import com.medlink.be.dto.PersonResDto;
import com.medlink.be.entity.Person;
import com.medlink.be.exception.ResourceNotFoundException;
import com.medlink.be.repository.PersonRepo;
import com.medlink.be.util.LanguageUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock
  private PersonRepo personRepo;

  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private PersonService personService;

  @Test
  void findAll_returnsPageData() {
    var person = samplePerson();
    var pageable = PageRequest.of(0, 10);
    var page = new PageImpl<>(List.of(person), pageable, 1);
    when(personRepo.findAll(
        org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<Person>>any(),
        eq(pageable)
    )).thenReturn(page);

    PaginatedDto<PersonResDto> result = personService.findAll(null, null, null, 1, 10);

    assertThat(result.items()).hasSize(1);
    assertThat(result.items().getFirst().firstName()).isEqualTo(person.getFirstName());
    assertThat(result.meta().page()).isEqualTo(1); // meta uses 1-based for response
    verify(personRepo, times(1)).findAll(
        org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<Person>>any(),
        eq(pageable)
    );
  }

  @Test
  void findById_returnsPerson() {
    var person = samplePerson();
    when(personRepo.findById(1L)).thenReturn(Optional.of(person));

    PersonResDto result = personService.findById(1L);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(person.getId());
    verify(personRepo, times(1)).findById(1L);
  }

  @Test
  void findById_returnsNullWhenMissing() {
    when(personRepo.findById(99L)).thenReturn(Optional.empty());

    PersonResDto result = personService.findById(99L);

    assertThat(result).isNull();
  }

  @Test
  void create_persistsAndReturnsDto() {
    var req = sampleReq();
    var saved = samplePerson();
    when(personRepo.save(any(Person.class))).thenReturn(saved);

    PersonResDto result = personService.create(req);

    assertThat(result.id()).isEqualTo(saved.getId());
    assertThat(result.firstName()).isEqualTo(saved.getFirstName());
    verify(personRepo, times(1)).save(any(Person.class));
  }

  @Test
  void update_updatesFields() {
    var existing = samplePerson();
    when(personRepo.findById(1L)).thenReturn(Optional.of(existing));
    when(personRepo.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var updatedReq = new PersonReqDto(
        "Updated",
        "User",
        LocalDate.of(1990, 2, 2),
        Gender.MALE,
        "+62123456789",
        "New Address",
        "New Suburb",
        "New State",
        "2222"
    );

    PersonResDto result = personService.update(1L, updatedReq);

    assertThat(result.firstName()).isEqualTo("Updated");
    assertThat(result.gender()).isEqualTo(Gender.MALE);
    assertThat(result.postcode()).isEqualTo("2222");
    verify(personRepo, times(1)).save(any(Person.class));
  }

  @Test
  void update_throwsWhenNotFound() {
    when(personRepo.findById(42L)).thenReturn(Optional.empty());
    ReflectionTestUtils.setField(LanguageUtil.class, "messageSource", messageSource);
    when(messageSource.getMessage(
        eq("error.person.not-found"),
        any(Object[].class),
        any()))
        .thenReturn("Person not found");

    assertThatThrownBy(() -> personService.update(42L, sampleReq()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Person not found");
  }

  @Test
  void deleteFlagById_callsRepo() {
    personService.deleteFlagById(5L);
    verify(personRepo, times(1)).deleteFlagById(5L);
  }

  private PersonReqDto sampleReq() {
    return new PersonReqDto(
        "Abu",
        "Bakar",
        LocalDate.of(1993, 8, 17),
        Gender.MALE,
        "+6281234567890",
        "Jl. Merdeka No. 10",
        "Menteng",
        "DKI Jakarta",
        "10310"
    );
  }

  private Person samplePerson() {
    var person = new Person(sampleReq());
    person.setId(1L);
    person.setPid("P000000001");
    return person;
  }
}
