package com.medlink.be.entity;

import com.medlink.be.constant.Gender;
import com.medlink.be.dto.PersonReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "person")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Person extends BaseEntity {

  @Column(name = "pid", nullable = false, updatable = false, length = 9)
  @Generated
  private String pid;

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(nullable = false, length = 13)
  private String lastName;

  @Column(nullable = false)
  private LocalDate dateOfBirth;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(nullable = false, length = 15)
  private String phone;

  @Column(nullable = false, length = 50)
  private String address;

  @Column(nullable = false)
  private String suburb;

  @Column(nullable = false)
  private String state;

  @Column(nullable = false, length = 4)
  private String postcode;

  public Person(PersonReqDto reqDto) {
    this.firstName = reqDto.firstName();
    this.lastName = reqDto.lastName();
    this.dateOfBirth = reqDto.dateOfBirth();
    this.gender = reqDto.gender();
    this.phone = reqDto.phone();
    this.address = reqDto.address();
    this.suburb = reqDto.suburb();
    this.state = reqDto.state();
    this.postcode = reqDto.postcode();
  }

  @PrePersist
  @PreUpdate
  @PostLoad
  private void normalizePhone() {
    phone = phone == null ? null : phone.replaceAll("\\D+", "");
  }
}
