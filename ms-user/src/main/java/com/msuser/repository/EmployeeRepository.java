package com.msuser.repository;

import com.msuser.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAuthUserId(Long authUserId);
    Optional<Employee> findByEmployeeCode(String employeeCode);
    Optional<Employee> findByDocumentNumber(String documentNumber);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByDocumentNumber(String documentNumber);
    List<Employee> findByAreaId(Long areaId);
    Page<Employee> findByActiveTrue(Pageable pageable);
    Page<Employee> findByAreaIdAndActiveTrue(Long areaId, Pageable pageable);
}
