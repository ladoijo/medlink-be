package com.medlink.be.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.medlink.be.constant.Endpoint;
import com.medlink.be.constant.Gender;
import com.medlink.be.dto.PaginatedDto;
import com.medlink.be.dto.PersonReqDto;
import com.medlink.be.dto.PersonResDto;
import com.medlink.be.service.PersonService;
import com.medlink.be.util.LanguageUtil;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(PersonController.class)
@Import(LanguageUtil.class)
class PersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private PersonService personService;

  @Test
  void findAll_returnsPagedPersons() throws Exception {
    var person = samplePersonResDto();
    var meta = new PaginatedDto.PageMeta(1, 10, 1, 1, true, true, false, false);
    var paginated = new PaginatedDto<>(List.of(person), meta);
    when(personService.findAll(null, null, null, 0, 10)).thenReturn(paginated);

    mockMvc.perform(get("/api" + Endpoint.PERSON_V1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.items[0].id").value(person.id()))
        .andExpect(jsonPath("$.data.items[0].firstName").value(person.firstName()))
        .andExpect(jsonPath("$.data.meta.page").value(meta.page()));
  }

  @Test
  void findById_returnsPerson() throws Exception {
    var person = samplePersonResDto();
    when(personService.findById(1L)).thenReturn(person);

    mockMvc.perform(get("/api" + Endpoint.PERSON_BY_ID_V1, 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(person.id()))
        .andExpect(jsonPath("$.data.pid").value(person.pid()));
  }

  @Test
  void create_persistsPerson() throws Exception {
    var req = samplePersonReqDto();
    var person = samplePersonResDto();
    when(personService.create(any(PersonReqDto.class))).thenReturn(person);

    mockMvc.perform(post("/api" + Endpoint.PERSON_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(person.id()));

    verify(personService, times(1)).create(any(PersonReqDto.class));
  }

  @Test
  void update_updatesExistingPerson() throws Exception {
    var req = samplePersonReqDto();
    var person = samplePersonResDto();
    when(personService.update(eq(1L), any(PersonReqDto.class))).thenReturn(person);

    mockMvc.perform(put("/api" + Endpoint.PERSON_BY_ID_V1, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(person.id()))
        .andExpect(jsonPath("$.data.firstName").value(person.firstName()));

    verify(personService, times(1)).update(eq(1L), any(PersonReqDto.class));
  }

  @Test
  void delete_softDeletesPerson() throws Exception {
    mockMvc.perform(delete("/api" + Endpoint.PERSON_BY_ID_V1, 1L))
        .andExpect(status().isOk());

    verify(personService, Mockito.times(1)).deleteFlagById(1L);
  }

  private PersonResDto samplePersonResDto() {
    return new PersonResDto(
        1L,
        "P000000001",
        "Alya",
        "Satria",
        LocalDate.of(1993, 8, 17),
        Gender.FEMALE,
        "+6281234567890",
        "Jl. Merdeka No. 10",
        "Menteng",
        "DKI Jakarta",
        "10310"
    );
  }

  private PersonReqDto samplePersonReqDto() {
    return new PersonReqDto(
        "Alya",
        "Satria",
        LocalDate.of(1993, 8, 17),
        Gender.FEMALE,
        "+6281234567890",
        "Jl. Merdeka No. 10",
        "Menteng",
        "DKI Jakarta",
        "10310"
    );
  }
}
