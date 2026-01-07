package com.medlink.be.repository;

import com.medlink.be.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

  @Modifying
  @Query(value = "UPDATE person SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
  void deleteFlagById(Long id);
}
