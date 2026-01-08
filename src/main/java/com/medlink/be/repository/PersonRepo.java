package com.medlink.be.repository;

import com.medlink.be.entity.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

  @Transactional
  @Modifying
  @Query(value = "UPDATE medlink.person SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
  void deleteFlagById(@Param("id") Long id);
}
