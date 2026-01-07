package com.medlink.be.controller;

import com.medlink.be.constant.Endpoint;
import com.medlink.be.dto.ApiRespDto;
import com.medlink.be.dto.PaginatedDto;
import com.medlink.be.dto.PersonReqDto;
import com.medlink.be.dto.PersonResDto;
import com.medlink.be.service.PersonService;
import com.medlink.be.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

  private final PersonService service;

  @GetMapping(Endpoint.PERSON_V1)
  public ResponseEntity<ApiRespDto<PaginatedDto<PersonResDto>>> findAll(
      @RequestParam(required = false) String pid,
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String lastName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    var data = service.findAll(pid, firstName, lastName, page, size);
    return ResponseUtil.okWithData(data);
  }

  @GetMapping(Endpoint.PERSON_BY_ID_V1)
  public ResponseEntity<ApiRespDto<PersonResDto>> findById(@PathVariable Long id) {
    var data = service.findById(id);
    return ResponseUtil.okWithData(data);
  }

  @PostMapping(Endpoint.PERSON_V1)
  public ResponseEntity<ApiRespDto<PersonResDto>> save(@Valid @RequestBody PersonReqDto reqDto) {
    var data = service.create(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @PutMapping(Endpoint.PERSON_BY_ID_V1)
  public ResponseEntity<ApiRespDto<PersonResDto>> update(@PathVariable Long id,
      @Valid @RequestBody PersonReqDto reqDto) {
    var data = service.update(id, reqDto);
    return ResponseUtil.okWithData(data);
  }

  @DeleteMapping(Endpoint.PERSON_BY_ID_V1)
  public ResponseEntity<ApiRespDto<?>> deleteFlagById(@PathVariable Long id) {
    service.deleteFlagById(id);
    return ResponseUtil.ok();
  }
}
